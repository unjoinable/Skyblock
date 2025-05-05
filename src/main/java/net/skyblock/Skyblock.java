package net.skyblock;

import net.skyblock.registry.base.Registries;
import net.skyblock.registry.impl.HandlerRegistry;
import net.skyblock.registry.impl.ItemRegistry;
import net.skyblock.registry.impl.ReforgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

/**
 * Main class for the Skyblock server implementation.
 * This class holds the registry access points and core components while delegating
 * initialization and server operation to ServerBootstrap.
 */
public class Skyblock implements Registries {
    private static Skyblock instance;
    private final DependencyContainer container;
    private final ServerBootstrap serverBootstrap;
    private boolean isRunning = false;

    /**
     * Main entry point for the Skyblock server.
     */
    public static void main(String[] args) {
        Logger.info("Starting Skyblock application");
        try {
            instance = new Skyblock();
            instance.getServerBootstrap().start("0.0.0.0", 25565);
        } catch (Exception e) {
            Logger.error(e, "Failed to start Skyblock server");
            System.exit(1);
        }
    }

    /**
     * Constructs a new Skyblock server instance.
     */
    public Skyblock() {
        Logger.info("Creating Skyblock instance");
        this.container = new DependencyContainer();
        this.serverBootstrap = new ServerBootstrap(this);
    }

    /**
     * Returns the skyblock instance
     */
    public static @NotNull Skyblock getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Skyblock instance has not been initialized");
        }
        return instance;
    }

    public @NotNull ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public @NotNull DependencyContainer getContainer() {
        return container;
    }

    @Override
    public @NotNull ItemRegistry items() {
        return container.get(ItemRegistry.class);
    }

    @Override
    public @NotNull HandlerRegistry handlers() {
        return container.get(HandlerRegistry.class);
    }

    @Override
    public @NotNull ReforgeRegistry reforges() {
        return container.get(ReforgeRegistry.class);
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public boolean isRunning() {
        return isRunning;
    }
}