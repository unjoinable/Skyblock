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
     * Constructs a new Skyblock server instance and initializes all registries.
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

    public @NotNull ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public @NotNull ReforgeRegistry getReforgeRegistry() {
        return reforgeRegistry;
    }

    public AttributeCodecRegistry getCodecRegistry() {
        return codecRegistry;
    }

    public boolean isRunning() {
        return isRunning;
    }
}