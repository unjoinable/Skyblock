package net.unjoinable.skyblock.event.listener.player.combat;

import net.unjoinable.skyblock.combat.damage.DamageReason;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.combat.statistic.Statistic;
import net.unjoinable.skyblock.event.custom.PlayerDamageEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class PlayerDamageListener implements Consumer<PlayerDamageEvent> {
    private static final Random RANDOM = ThreadLocalRandom.current();

    @Override
    public void accept(PlayerDamageEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        SkyblockDamage originalDamage = event.getDamage();

        if (originalDamage.damageReason() == DamageReason.FEROCITY) return;

        double ferocity = player.getStatSystem().getStat(Statistic.FEROCITY);
        int guaranteedHits = (int) (ferocity / 100);
        double chanceForExtraHit = ferocity % 100;

        SkyblockDamage ferocityDamage = originalDamage.withReason(DamageReason.FEROCITY);

        for (int i = 0; i < guaranteedHits; i++) {
            event.getTarget().damage(ferocityDamage);
        }

        if (RANDOM.nextDouble(100) < chanceForExtraHit) {
            event.getTarget().damage(ferocityDamage);
        }
    }
}
