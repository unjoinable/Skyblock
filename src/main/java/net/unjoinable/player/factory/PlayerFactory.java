package net.unjoinable.player.factory;

import net.minestom.server.entity.Player;
import net.minestom.server.network.PlayerProvider;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.unjoinable.item.service.ItemProcessor;
import net.unjoinable.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerFactory implements PlayerProvider {
    private final ItemProcessor itemProcessor;

    public PlayerFactory(@NotNull ItemProcessor itemProcessor) {
        this.itemProcessor = itemProcessor;
    }

    @Override
    public @NotNull Player createPlayer(@NotNull PlayerConnection connection, @NotNull GameProfile gameProfile) {
        return new SkyblockPlayer(connection, gameProfile);
    }
}
