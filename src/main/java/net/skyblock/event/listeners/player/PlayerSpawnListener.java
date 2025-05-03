package net.skyblock.event.listeners.player;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawnListener implements EventListener<PlayerSpawnEvent> {

    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

        if (event.isFirstSpawn()) {
            player.initTaskLoop();
        }
        return Result.SUCCESS;
    }
}
