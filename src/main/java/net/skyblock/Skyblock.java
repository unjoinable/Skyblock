package net.skyblock;

import net.skyblock.registry.impl.AttributeCodecRegistry;
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
    private final ItemRegistry itemRegistry;
    private final ReforgeRegistry reforgeRegistry;
    private final AttributeCodecRegistry codecRegistry;

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
     * Initializes a new Skyblock server instance and sets up core registries and server bootstrap.
     */
    public Skyblock() {
        Logger.info("Initializing Skyblock instance");

        // Create registry instances but don't initialize them yet
        this.reforgeRegistry = new ReforgeRegistry();
        this.codecRegistry = new AttributeCodecRegistry();
        this.itemRegistry = new ItemRegistry(codecRegistry);

        // Create bootstrap with this instance - registration will happen there
        this.serverBootstrap = new ServerBootstrap(this);
    }

    /**
     * Starts the Skyblock server on the given address and port.
     *
     * @param address the network address to bind the server to
     * @param port the port number for the server to listen on
     */
    public void start(String address, int port) {
        Logger.info("Starting server on {}:{}", address, port);
        serverBootstrap.start(address, port);
        this.isRunning = true;
    }

    /**
     * Returns the item registry used by the server.
     *
     * @return the current {@link ItemRegistry} instance
     */
    public @NotNull ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    /**
     * Returns the registry containing all reforges available on the server.
     *
     * @return the ReforgeRegistry instance
     */
    public @NotNull ReforgeRegistry getReforgeRegistry() {
        return reforgeRegistry;
    }

    /**
     * Returns the attribute codec registry used by the server.
     *
     * @return the current AttributeCodecRegistry instance
     */
    public AttributeCodecRegistry getCodecRegistry() {
        return codecRegistry;
    }

    /**
     * Returns whether the Skyblock server is currently running.
     *
     * @return {@code true} if the server is running; {@code false} otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }
}