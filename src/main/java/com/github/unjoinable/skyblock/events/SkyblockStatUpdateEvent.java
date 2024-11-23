package com.github.unjoinable.skyblock.events;

import com.github.unjoinable.skyblock.statistics.holders.StatValueMap;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class SkyblockStatUpdateEvent implements Event, CancellableEvent, PlayerEvent {
    private boolean isCancelled = false;
    private final SkyblockPlayer player;
    private final StatValueMap stats;

    public SkyblockStatUpdateEvent(@NotNull SkyblockPlayer player, @NotNull StatValueMap stats) {
        this.player = player;
        this.stats = stats;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @Override
    public @NotNull SkyblockPlayer getPlayer() {
        return player;
    }

    public @NotNull StatValueMap getStats() {
        return stats;
    }
}
