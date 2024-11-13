package io.github.unjoinable.skyblock.listeners;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerStartSneakingEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerStartSneakingListener implements EventListener<PlayerStartSneakingEvent> {
    @Override
    public @NotNull Class<PlayerStartSneakingEvent> eventType() {
        return null;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerStartSneakingEvent event) {
        return null;
    }
}
