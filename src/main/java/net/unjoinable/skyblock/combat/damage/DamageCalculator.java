package net.unjoinable.skyblock.combat.damage;

import net.minestom.server.entity.Entity;
import net.unjoinable.skyblock.combat.statistic.StatProfile;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.systems.PlayerStatSystem;

import java.util.concurrent.ThreadLocalRandom;

import static net.unjoinable.skyblock.combat.statistic.Statistic.*;

/**
 * Handles damage calculations for combat scenarios in Skyblock.
 * Calculates melee damage based on player statistics including base damage,
 * strength, critical chance, and critical damage modifiers.
 */
public class DamageCalculator {
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final double BASE_PHYSICAL_VALUE = 5.0;

    private final PlayerStatSystem statSystem;
    private final SkyblockPlayer player;

    /**
     * Creates a new DamageCalculator for the specified player.
     *
     * @param player the player whose damage will be calculated
     * @param statSystem the system managing player statistics
     */
    public DamageCalculator(SkyblockPlayer player, PlayerStatSystem statSystem) {
        this.player = player;
        this.statSystem = statSystem;
    }

    /**
     * Calculates melee damage against a target entity.
     * Applies base damage, strength multipliers, and critical hit calculations.
     *
     * @param target the entity being attacked
     * @return SkyblockDamage object containing damage details
     */
    public SkyblockDamage calcMeleeDamage(Entity target) {
        StatProfile stats = statSystem.getFinalStats();
        double baseDamage = stats.get(DAMAGE);
        double strength = stats.get(STRENGTH);
        double critChance = stats.get(CRIT_CHANCE);
        double critDamage = stats.get(CRIT_DAMAGE);

        boolean isCritical = RANDOM.nextDouble() * 100.0 <= critChance;
        double calculatedDamage = (BASE_PHYSICAL_VALUE + baseDamage) * (1.0 + strength / 100.0);

        if (isCritical) {
            calculatedDamage *= (1.0 + critDamage / 100.0);
        }

        return SkyblockDamage
                .builder()
                .rawDamage(calculatedDamage)
                .damager(player)
                .target(target)
                .isCritical(isCritical)
                .damageReason(DamageReason.PLAYER)
                .build();
    }

    /**
     * Calculates the final applicable damage after defense reduction.
     * Applies defense formula: damage * (1 - defense / (defense + 100))
     *
     * @param damage the raw damage to be reduced
     * @return the final damage after defense calculations
     */
    public double calcApplicableDamage(SkyblockDamage damage) {
        if (damage.damageType().bypassesDefense()) {
            return damage.rawDamage();
        }

        double defense = statSystem.getFinalStats().get(DEFENSE);
        return damage.rawDamage() * (1.0 - (defense / (defense + 100.0)));
    }
}
