package net.unjoinable.skyblock;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
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
import net.unjoinable.skyblock.event.custom.PlayerDamageEvent;
import net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent;
import net.unjoinable.skyblock.event.listener.EntityListener;
import net.unjoinable.skyblock.event.listener.player.chat.PlayerChatListener;
import net.unjoinable.skyblock.event.listener.player.combat.*;
import net.unjoinable.skyblock.event.listener.player.connection.*;
import net.unjoinable.skyblock.event.listener.player.interaction.*;
import net.unjoinable.skyblock.event.listener.player.inventory.*;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.level.IslandManager;
import net.unjoinable.skyblock.player.factory.PlayerFactory;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import net.unjoinable.skyblock.registry.registries.ItemRegistry;
import net.unjoinable.skyblock.time.SkyblockStandardTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minestom.server.MinecraftServer.*;

public final class Skyblock {
    private static final Logger LOGGER = LoggerFactory.getLogger(Skyblock.class);
    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final int DEFAULT_PORT = 25565;
    private static final String BRAND_NAME = "Hystorm";

    private Skyblock() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static void main(String[] args) {
        LOGGER.info("Starting Skyblock server bootstrap...");

        try {
            var server = initializeServer();
            server.start(DEFAULT_HOST, DEFAULT_PORT);
        } catch (Exception e) {
            LOGGER.error("Failed to bootstrap server", e);
            System.exit(1);
        }
    }

    private static MinecraftServer initializeServer() {
        var server = MinecraftServer.init();
        var registries = createRegistries();
        var services = createServices(registries);
        configureServer(services);

        registerEventListeners(services.islandManager());
        registerCommands(registries.itemRegistry(), services.itemProcessor());

        return server;
    }

    private static ServerRegistries createRegistries() {
        LOGGER.info("Loading registries...");
        return new ServerRegistries(ItemRegistry.withDefaults(), CodecRegistry.withDefaults());
    }

    private static ServerServices createServices(ServerRegistries registries) {
        LOGGER.info("Initializing server components...");
        var itemProcessor = new ItemProcessor(registries.codecRegistry(), registries.itemRegistry());
        var skyblockTime = new SkyblockStandardTime();
        var islandManager = IslandManager.withDefaults();

        return new ServerServices(itemProcessor, skyblockTime, islandManager);
    }

    private static void configureServer(ServerServices services) {
        MojangAuth.init();
        getConnectionManager().setPlayerProvider(new PlayerFactory(services.itemProcessor(), services.skyblockTime()));
        MinecraftServer.setBrandName(BRAND_NAME);
    }

    private static void registerCommands(ItemRegistry itemRegistry, ItemProcessor itemProcessor) {
        LOGGER.info("Registering commands...");
        var commandManager = getCommandManager();

        commandManager.register(new TestCommand());
        commandManager.register(new ItemCommand(itemRegistry, itemProcessor));
        commandManager.register(new RankCommand());
        commandManager.register(new ICanHasStormCommand());
    }

    private static void registerEventListeners(IslandManager islandManager) {
        LOGGER.info("Registering event listeners...");
        var eventHandler = getGlobalEventHandler();

        registerPlayerEvents(eventHandler, islandManager);
        registerInventoryEvents(eventHandler);
        registerItemEvents(eventHandler);

        new EntityListener(eventHandler).register();
    }

    private static void registerPlayerEvents(GlobalEventHandler eventHandler, IslandManager islandManager) {
        eventHandler
                .addListener(PlayerSpawnEvent.class, new PlayerSpawnListener())
                .addListener(AsyncPlayerConfigurationEvent.class, new AsyncPlayerConfigurationListener(islandManager))
                .addListener(PlayerSwapItemEvent.class, new PlayerSwapItemListener())
                .addListener(PlayerUseItemEvent.class, new PlayerUseItemListener())
                .addListener(PlayerChatEvent.class, new PlayerChatListener())
                .addListener(PlayerChangeHeldSlotEvent.class, new PlayerChangeHeldSlotListener())
                .addListener(PlayerHandAnimationEvent.class, new PlayerHandAnimationListener())
                .addListener(PlayerBlockInteractEvent.class, new PlayerBlockInteractListener())
                .addListener(PlayerEntityInteractEvent.class, new PlayerEntityInteractListener())
                .addListener(PlayerLeftClickEvent.class, new PlayerLeftClickListener())
                .addListener(PlayerStartDiggingEvent.class, new PlayerStartDiggingListener())
                .addListener(PlayerCancelDiggingEvent.class, new PlayerCancelDiggingListener())
                .addListener(PlayerFinishDiggingEvent.class, new PlayerFinishDiggingListener())
                .addListener(PlayerBeginItemUseEvent.class, new PlayerBeginItemUseListener())
                .addListener(PlayerDamageEvent.class, new PlayerDamageListener());
    }

    private static void registerInventoryEvents(GlobalEventHandler eventHandler) {
        eventHandler
                .addListener(InventoryPreClickEvent.class, new InventoryPreClickListener())
                .addListener(InventoryClickEvent.class, new InventoryClickListener());
    }

    private static void registerItemEvents(GlobalEventHandler eventHandler) {
        eventHandler.addListener(ItemDropEvent.class, new ItemDropListener());
    }

    private record ServerRegistries(
            ItemRegistry itemRegistry,
            CodecRegistry codecRegistry) {}

    private record ServerServices(
            ItemProcessor itemProcessor,
            SkyblockStandardTime skyblockTime,
            IslandManager islandManager) {}
}