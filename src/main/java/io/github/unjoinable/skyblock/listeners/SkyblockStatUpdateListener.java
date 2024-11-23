package io.github.unjoinable.skyblock.listeners;

import io.github.unjoinable.skyblock.events.SkyblockStatUpdateEvent;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.statistics.holders.StatValueMap;
import net.minestom.server.event.EventListener;
import org.jetbrains.annotations.NotNull;

public class SkyblockStatUpdateListener implements EventListener<SkyblockStatUpdateEvent> {

    @Override
    public @NotNull Class<SkyblockStatUpdateEvent> eventType() {
        return SkyblockStatUpdateEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull SkyblockStatUpdateEvent event) {
        StatValueMap stats = event.getStats();

        for (Statistic stat : Statistic.getValues()) {
            if (stat.isCapped()) {
                double value = stats.get(stat);
                stats.put(stat, Math.min(value, stat.getCapValue()));
            }
        }
        return Result.SUCCESS;
    }
}
