package net.unjoinable.skyblock.player.systems;

import net.minestom.server.entity.Entity;
import net.unjoinable.skyblock.combat.damage.DamageCalculator;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.item.ability.traits.MagicAbility;
import net.unjoinable.skyblock.player.PlayerSystem;
import net.unjoinable.skyblock.player.SkyblockPlayer;

/**
 * Manages combat mechanics for a Skyblock player.
 *
 * <p>This system handles damage dealing and receiving, including damage calculations,
 * invulnerability checks, and player death. It integrates with the player's stat system
 * to determine combat outcomes.
 */
public class CombatSystem implements PlayerSystem {
    private final SkyblockPlayer player;
    private final PlayerStatSystem statSystem;
    private final DamageCalculator damageCalc;
    private boolean initialized;

    public CombatSystem(SkyblockPlayer player) {
        this.player = player;
        this.statSystem = player.getStatSystem();
        this.damageCalc = new DamageCalculator(player, statSystem);
    }

    /**
     * Applies damage to the player.
     *
     * <p>Calculates applicable damage and reduces player health. If the player
     * becomes invulnerable or dies during damage calculation, the damage is ignored.
     * If health reaches zero, the player is killed.
     *
     * @param damage the damage to apply
     */
    public void damage(SkyblockDamage damage) {
        if (statSystem.isInvulnerable() || player.isDead()) {
            return;
        }
        if (!statSystem.consumeHealth(damageCalc.calcApplicableDamage(damage))) {
            player.kill();
        }
    }

    /**
     * Calculates melee damage against a target entity.
     *
     * @param target the entity being attacked
     * @return the calculated damage to be applied
     */
    public SkyblockDamage attack(Entity target) {
        return this.damageCalc.calcMeleeDamage(target);
    }

    public SkyblockDamage magicAttack(Entity target, MagicAbility ability) {
        return this.damageCalc.calcAbilityDamage(target, ability);
    }

    @Override
    public void start() {
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
}
