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
import net.skyblock.event.listeners.item.ComponentAddListener;
import net.skyblock.event.listeners.item.ComponentRemoveListener;
import net.skyblock.event.listeners.item.ItemLoadListenerImpl;
import net.skyblock.event.listeners.player.*;
import net.skyblock.item.io.SkyblockItemLoader;
import net.skyblock.item.io.SkyblockItemProcessor;
import net.skyblock.player.SkyblockPlayer;
import net.skyblock.registry.base.Registries;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

/**
 * Server bootstrap class that handles initialization and startup of the Skyblock server
 * using dependency injection for registries.
 */
public class ServerBootstrap {
    private final MinecraftServer server;
    private final InstanceContainer hubInstance;
    private final SkyblockItemProcessor processor;
    private final Registries registries;
    private final Pos spawnPosition;

    /**
     * Creates a new server bootstrap with the given registries
     *
     * @param registries The registries to use for this server
     */
    public ServerBootstrap(Registries registries) {
        this(registries, new Pos(-2, 71, -68).withYaw(-180F), "hub");
    }

    /**
     * Creates a new server bootstrap with custom configuration
     *
     * @param registries The registries to use
     * @param spawnPosition The spawn position for players
     * @param worldPath The path to the world files for the hub
     */
    public ServerBootstrap(Registries registries, Pos spawnPosition, String worldPath) {
        this.server = MinecraftServer.init();
        this.registries = registries;
        this.spawnPosition = spawnPosition;

        // Set up Mojang authentication and player provider
        MojangAuth.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        // Load the hub world
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        this.hubInstance = loadWorld(instanceManager, worldPath);

        // Register all event listeners
        registerEventListeners();

        // Initialize registries in the correct order
        initRegistries();

        // Set up item processor
        this.processor = new SkyblockItemProcessor(registries.handlers(), registries.items());

        // Register commands
        registerCommands();

        Logger.info("Server bootstrap complete");
    }

    /**
     * Initializes all registries in the correct order
     */
    private void initRegistries() {
        Logger.info("Initializing registries...");

        registries.reforges().init();
        registries.handlers().init();
        registries.items().init();

        Logger.info("Registries initialized successfully");
    }

    /**
     * Registers all event listeners needed for the server
     */
    private void registerEventListeners() {
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
        eventHandler.addListener(new ComponentAddListener());
        eventHandler.addListener(new ComponentRemoveListener());

        // Register item load listener
        SkyblockItemLoader.addListener(new ItemLoadListenerImpl());

        Logger.info("Event listeners registered");
    }

    /**
     * Registers all commands for the server
     */
    private void registerCommands() {
        CommandManager cmdManager = MinecraftServer.getCommandManager();
        cmdManager.register(new ItemCommand(registries.items(), processor));
        cmdManager.register(new TestCommand());

        Logger.info("Commands registered");
    }

    /**
     * Loads a world from the specified path
     *
     * @param instanceManager The instance manager
     * @param worldPath The path to the world files
     * @return The loaded instance container
     */
    private @NotNull InstanceContainer loadWorld(InstanceManager instanceManager, String worldPath) {
        Logger.info("Loading world from '{}'", worldPath);
        InstanceContainer container = instanceManager.createInstanceContainer();
        container.setChunkLoader(new AnvilLoader(worldPath));
        return container;
    }

    /**
     * Starts the server on the specified address and port
     *
     * @param address The address to bind to
     * @param port The port to listen on
     */
    public void start(String address, int port) {
        Logger.info("Starting server on {}:{}", address, port);
        server.start(address, port);
    }

    /**
     * Gets the hub instance
     *
     * @return The hub instance
     */
    public @NotNull InstanceContainer getHubInstance() {
        return hubInstance;
    }

    /**
     * Gets the item processor
     *
     * @return The item processor
     */
    public @NotNull SkyblockItemProcessor getProcessor() {
        return processor;
    }

    /**
     * Gets the registries used by this server
     *
     * @return The registries
     */
    public @NotNull Registries getRegistries() {
        return registries;
    }

    /**
     * Gets the MinecraftServer instance
     *
     * @return The server instance
     */
    public @NotNull MinecraftServer getServer() {
        return server;
    }
}