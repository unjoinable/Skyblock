package net.skyblock.player;

import net.skyblock.player.rank.PlayerRank;
import net.skyblock.stats.StatHolder;
import net.skyblock.stats.StatProfile;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player in the Skyblock game, extending {@link Player} to include additional
 * stats and rank functionality.
 * <p>
 * This class handles the player's rank and stats management, and provides access to a {@link PlayerStatsManager}
 * for tracking and updating the player's stats in the Skyblock world.
 */
public class SkyblockPlayer extends Player implements StatHolder {
    private final PlayerStatsManager statsManager;
    private PlayerRank playerRank;

    /**
     * Constructs a {@link SkyblockPlayer} instance, initializing the player's connection,
     * game profile, and stats manager.
     * <p>
     * The player is assigned a default rank of {@link PlayerRank#DEFAULT}.
     *
     * @param playerConnection the connection of the player
     * @param gameProfile the game profile of the player
     */
    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        this.statsManager = new PlayerStatsManager(this);
        this.playerRank = PlayerRank.DEFAULT;
    }

    /**
     * Gets the stat profile associated with this player.
     * <p>
     * This method delegates the request to the player's {@link PlayerStatsManager} to retrieve the
     * player's statistics.
     *
     * @return the {@link StatProfile} of the player
     */
    @Override
    public @NotNull StatProfile getStatProfile() {
        return this.statsManager.getStatProfile();
    }

    /**
     * Sets the rank of this player.
     * <p>
     * This method allows for changing the player's rank to a new {@link PlayerRank}.
     *
     * @param playerRank the new rank to assign to this player
     */
    public void setPlayerRank(PlayerRank playerRank) {
        this.playerRank = playerRank;
    }

    /**
     * Gets the current rank of the player.
     *
     * @return the player's current {@link PlayerRank}
     */
    public PlayerRank getPlayerRank() {
        return playerRank;
    }

    /**
     * Gets the stats manager for this player.
     * <p>
     * The stats manager handles the player's stat profile and provides methods for updating
     * or retrieving individual stats.
     *
     * @return the {@link PlayerStatsManager} for the player
     */
    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }
}
