package com.github.unjoinable.skyblock.listeners;

import com.github.unjoinable.skyblock.player.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class AsyncPlayerConfigurationListener implements EventListener<AsyncPlayerConfigurationEvent> {
    private final Instance spawnInstance;
    private final Pos spawnPos;

    public AsyncPlayerConfigurationListener(Instance spawnInstance, Pos spawnPos) {
        this.spawnInstance = spawnInstance;
        this.spawnPos = spawnPos;
    }

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        final SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        event.setSpawningInstance(spawnInstance);
        player.setRespawnPoint(spawnPos);

        return Result.SUCCESS;
    }
}
