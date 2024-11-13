package io.github.unjoinable.skyblock.listeners;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.user.sidebar.DefaultScoreboard;
import io.github.unjoinable.skyblock.user.sidebar.DefaultSidebar;
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
        new DefaultSidebar(player).init();
        return Result.SUCCESS;
    }
}
