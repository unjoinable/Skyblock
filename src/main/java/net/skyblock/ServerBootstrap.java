package net.skyblock;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.skyblock.command.impl.ItemCommand;
import net.skyblock.command.impl.TestCommand;
import net.skyblock.event.listeners.entity.EntityAttackListener;
import net.skyblock.event.listeners.inventory.InventoryCloseListener;
import net.skyblock.event.listeners.inventory.InventoryPreClickListener;
import net.skyblock.event.listeners.item.AttributeAddListener;
import net.skyblock.event.listeners.item.AttributeRemoveListener;
import net.skyblock.event.listeners.player.*;
import net.skyblock.item.inventory.ItemProviderFactory;
import net.skyblock.item.io.ItemProcessor;
import net.skyblock.item.io.ItemProcessorImpl;
import net.skyblock.player.SkyblockPlayerProvider;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

/**
 * Server bootstrap class that handles initialization and startup of the Skyblock server.
 */
public class ServerBootstrap {
    private final MinecraftServer server;
    private final InstanceContainer hubInstance;
    private final ItemProcessor processor;
    private final Skyblock skyblock;
    private final Pos spawnPosition;

    // Providers
    private final ItemProviderFactory itemProviderFactory;

    /**
     * Creates a new server bootstrap with the given Skyblock instance
     *
     * @param skyblock The Skyblock instance
     */
    public ServerBootstrap(Skyblock skyblock) {
        this(skyblock, new Pos(-2, 71, -68).withYaw(-180F), "hub");
    }

    /**
     * Initializes and configures the Skyblock server with a custom spawn position and hub world.
     *
     * @param skyblock      the main Skyblock instance
     * @param spawnPosition the position where players will spawn in the hub world
     * @param worldPath     the file path to the hub world data
     */
    public ServerBootstrap(Skyblock skyblock, Pos spawnPosition, String worldPath) {
        Logger.info("Bootstrapping server...");
        this.server = MinecraftServer.init();
        this.skyblock = skyblock;
        this.spawnPosition = spawnPosition;

        // Initialize registries first
        initializeRegistries();

        // Load the hub world
        this.hubInstance = loadWorld(worldPath);

        // Register all event listeners
        registerEventListeners();

        // Setup
        this.processor = new ItemProcessorImpl(skyblock.getCodecRegistry(), skyblock.getItemRegistry());
        this.itemProviderFactory = new ItemProviderFactory(this.processor);

        // Set up Mojang authentication and player provider
        configureAuthentication();

        // Register commands
        registerCommands();

        Logger.info("Server bootstrap complete");
    }

    /**
     * Initializes the reforge, codec, and item registries for the Skyblock server.
     * Ensures all necessary registries are set up before server startup.
     */
    private void initializeRegistries() {
        Logger.info("Initializing registries...");

        skyblock.getReforgeRegistry().init();
        skyblock.getCodecRegistry().init();
        skyblock.getItemRegistry().init();

        Logger.info("Registries initialized successfully");
    }

    /**
     * Configures authentication and player provider
     */
    private void configureAuthentication() {
        Logger.info("Setting up authentication");
        MojangAuth.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(new SkyblockPlayerProvider(itemProviderFactory));

    }

    /**
     * Registers all required event listeners with the global event handler for player,
     * inventory, entity, and item attribute events.
     */
    private void registerEventListeners() {
        Logger.info("Registering event listeners");
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();

        // Register player-related listeners
        eventHandler.addListener(new AsyncPlayerConfigurationListener(hubInstance, spawnPosition));
        eventHandler.addListener(new PlayerSwapItemListener());
        eventHandler.addListener(new PlayerChangeHeldSlotListener());
        eventHandler.addListener(new PlayerUseItemListener());
        eventHandler.addListener(new PlayerSpawnListener());

        // Register inventory listeners
        eventHandler.addListener(new InventoryPreClickListener());
        eventHandler.addListener(new InventoryCloseListener());

        // Register entity listeners
        eventHandler.addListener(new EntityAttackListener());

        // Register component listeners
        eventHandler.addListener(new AttributeAddListener());
        eventHandler.addListener(new AttributeRemoveListener());

        Logger.info("Event listeners registered");
    }

    /**
     * Registers all commands for the server
     */
    private void registerCommands() {
        Logger.info("Registering commands");
        CommandManager cmdManager = MinecraftServer.getCommandManager();
        cmdManager.register(new ItemCommand(skyblock.getItemRegistry(), processor));
        cmdManager.register(new TestCommand());

        Logger.info("Commands registered");
    }

    /**
     * Loads a world from the specified path
     *
     * @param worldPath The path to the world files
     * @return The loaded instance container
     */
    private @NotNull InstanceContainer loadWorld(String worldPath) {
        Logger.info("Loading world from '{}'", worldPath);
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer container = instanceManager.createInstanceContainer();
        container.setChunkLoader(new AnvilLoader(worldPath));
        return container;
    }

    /**
     * Starts the server on the specified address and port
     *
     * @param address The address to bind to
     * @param port    The port to listen on
     */
    public void start(String address, int port) {
        Logger.info("Starting server on {}:{}", address, port);
        server.start(address, port);
    }
}