package net.unjoinable.skyblock;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.extras.MojangAuth;
import net.unjoinable.skyblock.command.ICanHasStormCommand;
import net.unjoinable.skyblock.command.ItemCommand;
import net.unjoinable.skyblock.command.RankCommand;
import net.unjoinable.skyblock.command.TestCommand;
import net.unjoinable.skyblock.event.listener.EntityListener;
import net.unjoinable.skyblock.event.listener.PlayerListener;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.level.IslandManager;
import net.unjoinable.skyblock.player.factory.PlayerFactory;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import net.unjoinable.skyblock.registry.registries.ItemRegistry;
import net.unjoinable.skyblock.time.SkyblockStandardTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the bootstrapping and startup of the Skyblock server.
 *
 * <p>This class is responsible for initializing all necessary components,
 * registries, managers, and services required for the server to function
 * properly.</p>
 */
public final class ServerBootstrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerBootstrapper.class);

    private ServerBootstrapper() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Bootstraps and starts the Skyblock server.
     *
     * <p>This method performs the complete server initialization sequence:
     * <ul>
     *   <li>Loads all registries</li>
     *   <li>Initializes the Minecraft server</li>
     *   <li>Sets up authentication</li>
     *   <li>Configures managers and processors</li>
     *   <li>Registers event listeners</li>
     *   <li>Registers commands</li>
     *   <li>Starts the server</li>
     * </ul>
     *
     * @param host the host address to bind the server to
     * @param port the port to bind the server to
     */
    public static void bootstrap(String host, int port) {
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
            new PlayerListener(islandManager, MinecraftServer.getGlobalEventHandler()).register();
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

    /**
     * Registers all server commands.
     *
     * @param itemRegistry the item registry for item-related commands
     * @param itemProcessor the item processor for item-related commands
     */
    private static void registerCommands(ItemRegistry itemRegistry, ItemProcessor itemProcessor) {
        CommandManager cmdMgr = MinecraftServer.getCommandManager();

        cmdMgr.register(new TestCommand());
        cmdMgr.register(new ItemCommand(itemRegistry, itemProcessor));
        cmdMgr.register(new RankCommand());
        cmdMgr.register(new ICanHasStormCommand());
    }
}