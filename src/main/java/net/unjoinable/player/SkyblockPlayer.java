package net.unjoinable.player;

import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class SkyblockPlayer extends Player {
    private SystemsManager systemsManager;

    public SkyblockPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public void setSystemsManager(SystemsManager systemsManager) {
        if (this.systemsManager != null) {
            throw new IllegalStateException("Systems manager already set");
        }
        this.systemsManager = systemsManager;
    }

    public SystemsManager getSystemsManager() {
        return systemsManager;
    }
}
