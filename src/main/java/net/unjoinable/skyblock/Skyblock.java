package net.unjoinable.skyblock;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.unjoinable.skyblock.command.ICanHasStormCommand;
import net.unjoinable.skyblock.command.ItemCommand;
import net.unjoinable.skyblock.command.RankCommand;
import net.unjoinable.skyblock.command.TestCommand;
import net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent;
import net.unjoinable.skyblock.event.listener.EntityListener;
import net.unjoinable.skyblock.event.listener.player.*;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.level.IslandManager;
import net.unjoinable.skyblock.player.factory.PlayerFactory;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import net.unjoinable.skyblock.registry.registries.ItemRegistry;
import net.unjoinable.skyblock.time.SkyblockStandardTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Skyblock {
    private static final Logger LOGGER = LoggerFactory.getLogger(Skyblock.class);

    private Skyblock() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static void main(String[] args) {
        String host = "0.0.0.0";
        int port = 25565;

        LOGGER.info("Starting Skyblock server bootstrap...");

        try {
            // Load registries
            LOGGER.info("Loading registries...");
            ItemRegistry itemRegistry = ItemRegistry.withDefaults();
            CodecRegistry codecRegistry = CodecRegistry.withDefaults();

            // Initialize core server components
            LOGGER.info("Initializing server components...");
            MinecraftServer server = MinecraftServer.init();
            MojangAuth.init();

            // Setup services and managers
            ItemProcessor itemProcessor = new ItemProcessor(codecRegistry, itemRegistry);
            SkyblockStandardTime skyblockTime = new SkyblockStandardTime();
            IslandManager islandManager = IslandManager.withDefaults();

            // Configure server
            MinecraftServer.getConnectionManager().setPlayerProvider(new PlayerFactory(itemProcessor, skyblockTime));
            MinecraftServer.setBrandName("Hystorm");

            // Register event listeners
            LOGGER.info("Registering event listeners...");
            registerEvents(islandManager);
            new EntityListener(MinecraftServer.getGlobalEventHandler()).register();

            // Register commands
            LOGGER.info("Registering commands...");
            registerCommands(itemRegistry, itemProcessor);

            // Start the server
            LOGGER.info("Starting server on {}:{}", host, port);
            server.start(host, port);
        } catch (Exception e) {
            LOGGER.error("Failed to bootstrap server", e);
            MinecraftServer.stopCleanly();
            System.exit(1);
        }
    }

    private static void registerCommands(ItemRegistry itemRegistry, ItemProcessor itemProcessor) {
        CommandManager cmdMgr = MinecraftServer.getCommandManager();

        cmdMgr.register(new TestCommand());
        cmdMgr.register(new ItemCommand(itemRegistry, itemProcessor));
        cmdMgr.register(new RankCommand());
        cmdMgr.register(new ICanHasStormCommand());
    }

    private static void registerEvents(IslandManager islandManager) {
        MinecraftServer.getGlobalEventHandler()
                .addListener(PlayerSpawnEvent.class, new PlayerSpawnListener())
                .addListener(AsyncPlayerConfigurationEvent.class, new AsyncPlayerConfigurationListener(islandManager))
                .addListener(PlayerSwapItemEvent.class, new PlayerSwapItemListener())
                .addListener(InventoryPreClickEvent.class, new InventoryPreClickListener())
                .addListener(PlayerUseItemEvent.class, new PlayerUseItemListener())
                .addListener(PlayerChatEvent.class, new PlayerChatListener())
                .addListener(PlayerChangeHeldSlotEvent.class, new PlayerChangeHeldSlotListener())
                .addListener(PlayerHandAnimationEvent.class, new PlayerHandAnimationListener())
                .addListener(ItemDropEvent.class, new ItemDropListener())
                .addListener(PlayerBlockInteractEvent.class, new PlayerBlockInteractListener())
                .addListener(InventoryClickEvent.class, new InventoryClickListener())
                .addListener(PlayerEntityInteractEvent.class, new PlayerEntityInteractListener())
                .addListener(PlayerLeftClickEvent.class, new PlayerLeftClickListener())
                .addListener(PlayerStartDiggingEvent.class, new PlayerStartDiggingListener())
                .addListener(PlayerCancelDiggingEvent.class, new PlayerCancelDiggingListener())
                .addListener(PlayerFinishDiggingEvent.class, new PlayerFinishDiggingListener())
                .addListener(PlayerBeginItemUseEvent.class, new PlayerBeginItemUseListener());
    }
}
