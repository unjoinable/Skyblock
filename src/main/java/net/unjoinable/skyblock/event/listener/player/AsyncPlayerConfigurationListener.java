package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.unjoinable.skyblock.level.IslandManager;
import net.unjoinable.skyblock.level.SkyblockIsland;
import net.unjoinable.skyblock.player.SkyblockPlayer;

import java.util.function.Consumer;

/**
 * Handles player configuration events during connection setup.
 */
public class AsyncPlayerConfigurationListener implements Consumer<AsyncPlayerConfigurationEvent> {
    private final IslandManager islandManager;

    public AsyncPlayerConfigurationListener(IslandManager islandManager) {
        this.islandManager = islandManager;
    }

    @Override
    public void accept(AsyncPlayerConfigurationEvent event) {
        Instance instance = islandManager.getInstance(SkyblockIsland.HUB);
        event.setSpawningInstance(instance);

        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        player.setRespawnPoint(SkyblockIsland.HUB.spawnPoint());
    }
}