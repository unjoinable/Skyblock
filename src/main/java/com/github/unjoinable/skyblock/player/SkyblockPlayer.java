package com.github.unjoinable.skyblock.player;

import com.github.unjoinable.skyblock.stats.StatHolder;
import com.github.unjoinable.skyblock.stats.StatProfile;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class SkyblockPlayer extends Player implements StatHolder {

    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    @Override
    public @NotNull StatProfile getStatProfile() {
        return null;
    }
}
