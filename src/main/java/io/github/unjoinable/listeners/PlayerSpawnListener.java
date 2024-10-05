package io.github.unjoinable.listeners;

import io.github.unjoinable.user.SkyblockPlayer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {
    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        SkyblockPlayer player = ((SkyblockPlayer) event.getPlayer());
        return Result.SUCCESS;
    }
}
