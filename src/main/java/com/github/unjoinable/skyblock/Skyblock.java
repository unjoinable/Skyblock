package com.github.unjoinable.skyblock;

import com.github.unjoinable.skyblock.player.SkyblockPlayer;
import com.github.unjoinable.skyblock.registry.Registry;
import net.minestom.server.MinecraftServer;

public class Skyblock {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        // Registries
        Registry.ITEM_REGISTRY.init();
        Registry.COMPONENT_REGISTRY.init();

        // Start
        server.start("0.0.0.0", 25565);
    }
}