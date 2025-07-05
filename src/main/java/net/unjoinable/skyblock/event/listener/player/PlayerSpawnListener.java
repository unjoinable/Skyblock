package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;

import java.util.function.Consumer;

/**
 * Handles player spawn events and initialization.
 */
public class PlayerSpawnListener implements Consumer<PlayerSpawnEvent> {

    @Override
    public void accept(PlayerSpawnEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (event.isFirstSpawn()) player.init();
    }
}