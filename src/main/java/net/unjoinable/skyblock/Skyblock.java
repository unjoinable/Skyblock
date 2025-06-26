package net.unjoinable.skyblock;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.unjoinable.skyblock.command.TestCommand;
import net.unjoinable.skyblock.event.listener.PlayerListener;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.player.factory.PlayerFactory;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import net.unjoinable.skyblock.registry.registries.ItemRegistry;

public class Skyblock {

    private Skyblock() {
        throw new AssertionError();
    }

    public static void main(String[] args) {
        // Registries
        var itemRegistry = ItemRegistry.withDefaults();
        var attributeCodecRegistry = CodecRegistry.withDefaults();

        // Systems
        ItemProcessor itemProcessor = new ItemProcessor(attributeCodecRegistry, itemRegistry);

        // Server
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(new PlayerFactory(itemProcessor));

        // Listeners
        new PlayerListener(MinecraftServer.getGlobalEventHandler()).register();

        MinecraftServer.getCommandManager().register(new TestCommand());

        server.start("0.0.0.0", 25565);
    }
}
