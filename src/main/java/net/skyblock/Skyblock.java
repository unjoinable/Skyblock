package net.skyblock;

import net.skyblock.registry.impl.HandlerRegistry;
import net.skyblock.registry.impl.ItemRegistry;
import net.skyblock.registry.impl.ReforgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

/**
 * Main class for the Skyblock server implementation.
 * This class acts as the central access point to all core systems and registries.
 */
public class Skyblock {

    // Core registries
    private final HandlerRegistry handlerRegistry;
    private final ItemRegistry itemRegistry;
    private final ReforgeRegistry reforgeRegistry;

    // Server components
    private final ServerBootstrap serverBootstrap;
    private boolean isRunning = false;

    /**
     * Main entry point for the Skyblock server.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Skyblock instance;
        Logger.info("Starting Skyblock server");
        try {
            instance = new Skyblock();
            instance.start("0.0.0.0", 25565);
        } catch (Exception e) {
            Logger.error(e, "Failed to start Skyblock server");
            System.exit(1);
        }
    }

    /**
     * Constructs a new Skyblock server instance and initializes all registries.
     */
    public Skyblock() {
        Logger.info("Initializing Skyblock instance");

        // Create registry instances but don't initialize them yet
        this.reforgeRegistry = new ReforgeRegistry();
        this.handlerRegistry = new HandlerRegistry();
        this.itemRegistry = new ItemRegistry(handlerRegistry);

        // Create bootstrap with this instance - registration will happen there
        this.serverBootstrap = new ServerBootstrap(this);
    }

    /**
     * Starts the server on the specified address and port.
     *
     * @param address The address to bind to
     * @param port The port to listen on
     */
    public void start(String address, int port) {
        Logger.info("Starting server on {}:{}", address, port);
        serverBootstrap.start(address, port);
        this.isRunning = true;
    }

    /**
     * Gets the item registry.
     *
     * @return The item registry
     */
    public @NotNull ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    /**
     * Gets the handler registry.
     *
     * @return The handler registry
     */
    public @NotNull HandlerRegistry getHandlerRegistry() {
        return handlerRegistry;
    }

    /**
     * Gets the reforge registry.
     *
     * @return The reforge registry
     */
    public @NotNull ReforgeRegistry getReforgeRegistry() {
        return reforgeRegistry;
    }

    /**
     * Checks if the server is currently running.
     *
     * @return true if the server is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }
}