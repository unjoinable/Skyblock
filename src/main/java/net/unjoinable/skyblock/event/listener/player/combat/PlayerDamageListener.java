package net.unjoinable.skyblock.event.listener.player.combat;

import net.unjoinable.skyblock.combat.damage.DamageReason;
import net.unjoinable.skyblock.combat.damage.DamageType;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.combat.statistic.Statistic;
import net.unjoinable.skyblock.event.custom.PlayerDamageEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.systems.CombatSystem;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Handles player damage events and applies ferocity-based additional hits.
 * Ferocity grants guaranteed extra hits (100 ferocity = 1 guaranteed hit)
 * and a chance for additional hits based on the remainder.
 */
public class PlayerDamageListener implements Consumer<PlayerDamageEvent> {
    private static final Random RANDOM = ThreadLocalRandom.current();

    /**
     * Processes player damage events and applies ferocity mechanics.
     * 
     * @param event The player damage event to process
     */
    @Override
    public void accept(PlayerDamageEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        CombatSystem combatSys = player.getCombatSystem();
        SkyblockDamage originalDamage = event.getDamage();

        if (originalDamage.damageReason() == DamageReason.FEROCITY) return;

        double ferocity = player.getStatSystem().getStat(Statistic.FEROCITY);
        int guaranteedHits = (int) (ferocity / 100);
        double chanceForExtraHit = ferocity % 100;

        SkyblockDamage ferocityDamage = originalDamage.withReason(DamageReason.FEROCITY);

        for (int i = 0; i < guaranteedHits; i++) {
            event.getTarget().damage(ferocityDamage);
            combatSys.playFerocitySound();
        }

        if (RANDOM.nextDouble(100) < chanceForExtraHit) {
            event.getTarget().damage(ferocityDamage);
            combatSys.playFerocitySound();
        }
    }
}
