package net.unjoinable.skyblock.player.factory;

import net.minestom.server.entity.Player;
import net.minestom.server.network.PlayerProvider;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * A factory responsible for creating {@link SkyblockPlayer} instances.
 *
 * <p>This class implements Minestom's {@link PlayerProvider} interface and is registered
 * with the server to control how player objects are instantiated when new connections occur.</p>
 *
 * <p>The {@link PlayerFactory} injects game-specific logic and services (e.g., {@link ItemProcessor})
 * into the {@link SkyblockPlayer} using a {@link PlayerCreationContext} to encapsulate required dependencies.</p>
 *
 * <p>Usage typically involves passing this factory to Minestom via
 * {@code MinecraftServer.setPlayerProvider(new PlayerFactory(...))}.</p>
 */
public class PlayerFactory implements PlayerProvider {
    private final ItemProcessor itemProcessor;

    /**
     * Constructs a new {@code PlayerFactory} with the required {@link ItemProcessor}.
     *
     * @param itemProcessor the service responsible for handling item logic in the game; must not be null
     */
    public PlayerFactory(@NotNull ItemProcessor itemProcessor) {
        this.itemProcessor = itemProcessor;
    }

    @Override
    public @NotNull Player createPlayer(@NotNull PlayerConnection connection, @NotNull GameProfile gameProfile) {
        PlayerCreationContext ctx = PlayerCreationContext.builder()
                .connection(connection)
                .gameProfile(gameProfile)
                .itemProcessor(itemProcessor)
                .build();

        return new SkyblockPlayer(ctx);
    }
}
