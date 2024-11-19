package io.github.unjoinable.skyblock.listeners;

import io.github.unjoinable.skyblock.events.SkyblockDamageEvent;
import io.github.unjoinable.skyblock.statistics.CombatEntity;
import io.github.unjoinable.skyblock.statistics.SkyblockDamage;
import net.minestom.server.event.EventListener;
import org.jetbrains.annotations.NotNull;

public class SkyblockDamageListener implements EventListener<SkyblockDamageEvent> {
    @Override
    public @NotNull Class<SkyblockDamageEvent> eventType() {
        return SkyblockDamageEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull SkyblockDamageEvent event) {
        CombatEntity source = event.getSource();
        CombatEntity target = event.getTarget();
        SkyblockDamage damage = event.getSkyblockDamage();

        if (!target.getEntity().isActive()) return Result.INVALID;

        target.applyDamage(event.getSkyblockDamage());
        return Result.SUCCESS;
    }
}
