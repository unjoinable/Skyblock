package net.skyblock.player;

import net.skyblock.player.rank.PlayerRank;
import net.skyblock.stats.StatHolder;
import net.skyblock.stats.StatProfile;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class SkyblockPlayer extends Player implements StatHolder {
    private final PlayerStatsManager statsManager;
    private PlayerRank playerRank;

    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
        this.statsManager = new PlayerStatsManager(this);
        this.playerRank = PlayerRank.DEFAULT;
    }

    @Override
    public @NotNull StatProfile getStatProfile() {
        return this.statsManager.getStatProfile();
    }

    public void setPlayerRank(PlayerRank playerRank) {
        this.playerRank = playerRank;
    }

    public PlayerRank getPlayerRank() {
        return playerRank;
    }

    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }
}
