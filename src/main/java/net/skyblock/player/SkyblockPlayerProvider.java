package net.skyblock.player;

import net.minestom.server.entity.Player;
import net.minestom.server.network.PlayerProvider;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.skyblock.item.inventory.ItemProviderFactory;
import net.skyblock.item.inventory.PlayerItemProvider;
import net.skyblock.player.manager.PlayerAbilityManager;
import net.skyblock.player.manager.PlayerStatsManager;
import net.skyblock.player.ui.SkyblockPlayerActionBar;
import org.jetbrains.annotations.NotNull;

public class SkyblockPlayerProvider implements PlayerProvider {
    private final ItemProviderFactory factory;

    public SkyblockPlayerProvider(@NotNull ItemProviderFactory factory) {
        this.factory = factory;
    }

    /**
     * Creates a new {@link Player} object based on his connection data.
     * <p>
     * Called once a client want to join the server and need to have an assigned player object.
     *
     * @param connection  the player connection
     * @param gameProfile the player game profile
     * @return a newly create {@link Player} object
     */
    @Override
    public @NotNull Player createPlayer(@NotNull PlayerConnection connection, @NotNull GameProfile gameProfile) {
        SkyblockPlayer player = new SkyblockPlayer(connection, gameProfile);

        // Dependencies / Systems
        PlayerItemProvider itemProvider = factory.createProvider(player);

        // Stats
        PlayerStatsManager playerStatsManager = new PlayerStatsManager(player, itemProvider);
        player.setStatsManager(playerStatsManager);

        // Ability
        PlayerAbilityManager playerAbilityManager = new PlayerAbilityManager(player);
        player.setAbilityManager(playerAbilityManager);

        // UI
        SkyblockPlayerActionBar actionBar = new SkyblockPlayerActionBar(player);
        player.setActionBar(actionBar);

        return player;
    }
}
