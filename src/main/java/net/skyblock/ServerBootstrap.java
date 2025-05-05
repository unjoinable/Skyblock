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
import net.skyblock.event.listeners.player.*;
import net.skyblock.item.io.SkyblockItemProcessor;
import net.skyblock.player.SkyblockPlayer;
import net.skyblock.registry.base.Registries;
import net.skyblock.registry.impl.HandlerRegistry;
import net.skyblock.registry.impl.ItemRegistry;
import net.skyblock.registry.impl.ReforgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Server bootstrap class that handles initialization and startup of the Skyblock server
 * using dependency injection for registries.
 */
public class ServerBootstrap {
    private final MinecraftServer server;
    private final InstanceContainer hubInstance;
    private final SkyblockItemProcessor processor;
    private final Pos spawnPosition;
    private final Skyblock skyblock;
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);

    /**
     * Creates a new server bootstrap with the given Skyblock instance
     */
    public ServerBootstrap(Skyblock skyblock) {
        this(skyblock, new Pos(-2, 71, -68).withYaw(-180F), "hub");
    }

    /**
     * Creates a new server bootstrap with custom configuration
     */
    public ServerBootstrap(Skyblock skyblock, Pos spawnPosition, String worldPath) {
        Logger.info("Initializing server bootstrap");
        this.server = MinecraftServer.init();
        this.skyblock = skyblock;
        this.spawnPosition = spawnPosition;

        setupRegistries(skyblock.getContainer());
        initializeDependencyContainer(skyblock.getContainer());
        setupMojangAuth();
        this.hubInstance = loadWorld(MinecraftServer.getInstanceManager(), worldPath);
        registerEventListeners(MinecraftServer.getGlobalEventHandler());
        this.processor = skyblock.getContainer().get(SkyblockItemProcessor.class);
        registerCommands(MinecraftServer.getCommandManager());

        isInitialized.set(true);
        Logger.info("Server bootstrap complete");
    }

    /**
     * Sets up all registries in the dependency container with proper dependency order.
     */
    private void setupRegistries(DependencyContainer container) {
        Logger.info("Setting up registries");
        container.register(ReforgeRegistry.class, ReforgeRegistry::new);
        container.register(HandlerRegistry.class, HandlerRegistry::new);
        container.register(ItemRegistry.class, () -> new ItemRegistry(container.get(HandlerRegistry.class)));

        // Register Skyblock and Registries instances for dependency injection
        container.registerInstance(Skyblock.class, skyblock);
        container.registerInstance(Registries.class, skyblock);
    }

    private void initializeDependencyContainer(DependencyContainer container) {
        Logger.info("Initializing dependency container");
        container.initialize();
    }

    private void setupMojangAuth() {
        Logger.info("Initializing Mojang authentication and player provider");
        MojangAuth.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);
    }

    private void registerEventListeners(GlobalEventHandler eventHandler) {
        Logger.info("Registering event listeners");

        // Player listeners
        eventHandler.addListener(new AsyncPlayerConfigurationListener(hubInstance, spawnPosition));
        eventHandler.addListener(new PlayerSwapItemListener());
        eventHandler.addListener(new PlayerChangeHeldSlotListener());
        eventHandler.addListener(new PlayerUseItemListener());
        eventHandler.addListener(new PlayerSpawnListener());

        // Inventory listeners
        eventHandler.addListener(new InventoryPreClickListener());
        eventHandler.addListener(new InventoryCloseListener());

        // Entity listeners
        eventHandler.addListener(new EntityAttackListener());

        // Component listeners
        eventHandler.addListener(new ComponentAddListener());
        eventHandler.addListener(new ComponentRemoveListener());

        Logger.info("Event listeners registered");
    }

    private void registerCommands(@NotNull CommandManager cmdManager) {
        Logger.info("Registering commands");
        cmdManager.register(new ItemCommand(skyblock.items(), processor));
        cmdManager.register(new TestCommand());
        Logger.info("Commands registered");
    }

    private @NotNull InstanceContainer loadWorld(InstanceManager instanceManager, String worldPath) {
        Logger.info("Loading world from '{}'", worldPath);
        InstanceContainer container = instanceManager.createInstanceContainer();
        container.setChunkLoader(new AnvilLoader(worldPath));
        Logger.info("World loaded successfully");
        return container;
    }

    public void start(String address, int port) {
        if (!isInitialized()) {
            throw new IllegalStateException("Server bootstrap is not properly initialized");
        }

        if (skyblock.isRunning()) {
            Logger.warn("Server is already running");
            return;
        }

        Logger.info("Starting server on {}:{}", address, port);
        server.start(address, port);
        skyblock.setRunning(true);
        Logger.info("Skyblock server started successfully on {}:{}", address, port);
    }

    public @NotNull SkyblockItemProcessor getProcessor() {
        return processor;
    }

    public @NotNull MinecraftServer getServer() {
        return server;
    }

    public @NotNull Skyblock getSkyblock() {
        return skyblock;
    }

    public boolean isInitialized() {
        return isInitialized.get();
    }
}