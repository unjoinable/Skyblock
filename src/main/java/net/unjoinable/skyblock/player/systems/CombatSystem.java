package net.unjoinable.skyblock.player.systems;

import net.minestom.server.entity.Entity;
import net.unjoinable.skyblock.combat.damage.DamageCalculator;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.player.PlayerSystem;
import net.unjoinable.skyblock.player.SkyblockPlayer;

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

    public void damage(SkyblockDamage damage) {
        if (statSystem.isInvulnerable() || player.isDead()) {
            return;
        }
        statSystem.consumeHealth(damageCalc.calcApplicableDamage(damage));
    }

    public SkyblockDamage attack(Entity target) {
        return this.damageCalc.calcMeleeDamage(target);
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
