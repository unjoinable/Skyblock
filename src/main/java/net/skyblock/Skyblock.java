package net.skyblock;

import net.skyblock.registry.base.Registries;
import net.skyblock.registry.impl.HandlerRegistry;
import net.skyblock.registry.impl.ItemRegistry;
import net.skyblock.registry.impl.ReforgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

/**
 * Main class for the Skyblock server implementation.
 * This class handles server initialization, registry loading, and world setup.
 * It serves as the entry point for the entire Skyblock server application.
 */
public class Skyblock implements Registries {
    private static Skyblock instance;
    private final ItemRegistry itemRegistry;
    private final HandlerRegistry handlerRegistry;
    private final ReforgeRegistry reforgeRegistry;
    private final ServerBootstrap serverBootstrap;

    /**
     * Main entry point for the Skyblock server.
     *
     * @param args Command line arguments (unused)
     */
    public static void main(String[] args) {
        Logger.info("Starting Skyblock application");
        instance = new Skyblock();
        instance.start("0.0.0.0", 25565);
    }

    /**
     * Constructs a new Skyblock server instance.
     * Initializes the server, registers event listeners, loads registries,
     * creates the hub world.
     */
    public Skyblock() {
        // Create registries
        this.handlerRegistry = new HandlerRegistry();
        this.itemRegistry = new ItemRegistry(handlerRegistry);
        this.reforgeRegistry = new ReforgeRegistry();

        // Bootstrap the server with our registries
        this.serverBootstrap = new ServerBootstrap(this);
    }

    /**
     * Starts the server on the specified address and port.
     *
     * @param address The address to bind to
     * @param port The port to listen on
     */
    public void start(String address, int port) {
        serverBootstrap.start(address, port);
    }

    /**
     * Gets the server bootstrap instance
     *
     * @return The server bootstrap
     */
    public @NotNull ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    /**
     * Returns the skyblock instance
     *
     * @return The skyblock instance
     */
    public static Skyblock getInstance() {
        return instance;
    }

    /**
     * Gets the item registry
     *
     * @return The item registry
     */
    @Override
    public @NotNull ItemRegistry items() {
        return itemRegistry;
    }

    /**
     * Gets the handler registry
     *
     * @return The handler registry
     */
    @Override
    public @NotNull HandlerRegistry handlers() {
        return handlerRegistry;
    }

    /**
     * Gets the reforge registry
     *
     * @return The reforge registry
     */
    @Override
    public @NotNull ReforgeRegistry reforges() {
        return reforgeRegistry;
    }
}