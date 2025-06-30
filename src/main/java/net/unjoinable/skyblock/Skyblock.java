package net.unjoinable.skyblock;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.extras.MojangAuth;
import net.unjoinable.skyblock.command.ICanHasStormCommand;
import net.unjoinable.skyblock.command.ItemCommand;
import net.unjoinable.skyblock.command.RankCommand;
import net.unjoinable.skyblock.command.TestCommand;
import net.unjoinable.skyblock.event.listener.PlayerListener;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.level.IslandManager;
import net.unjoinable.skyblock.player.factory.PlayerFactory;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import net.unjoinable.skyblock.registry.registries.ItemRegistry;
import net.unjoinable.skyblock.time.SkyblockStandardTime;

public class Skyblock {

    private Skyblock() {
        throw new AssertionError();
    }

    public static void main(String[] args) {
        // Registries
        var itemRegistry = ItemRegistry.withDefaults();
        var attributeCodecRegistry = CodecRegistry.withDefaults();

        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();

        ItemProcessor itemProcessor = new ItemProcessor(attributeCodecRegistry, itemRegistry);
        SkyblockStandardTime skyblockTime = new SkyblockStandardTime();

        MinecraftServer.getConnectionManager().setPlayerProvider(new PlayerFactory(itemProcessor, skyblockTime));
        MinecraftServer.setBrandName("Hystorm");
        IslandManager islandManager = IslandManager.withDefaults();

        // Listeners
        new PlayerListener(islandManager, MinecraftServer.getGlobalEventHandler()).register();

        // Commands
        CommandManager cmdMgr = MinecraftServer.getCommandManager();
        cmdMgr.register(new TestCommand());
        cmdMgr.register(new ItemCommand(itemRegistry, itemProcessor));
        cmdMgr.register(new RankCommand());
        cmdMgr.register(new ICanHasStormCommand());
        server.start("0.0.0.0", 25565);
    }
}
