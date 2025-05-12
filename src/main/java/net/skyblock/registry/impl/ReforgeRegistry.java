package net.skyblock.registry.impl;

import net.minestom.server.MinecraftServer;
import net.skyblock.item.definition.Reforge;
import net.skyblock.item.io.ReforgeLoader;
import net.skyblock.registry.Registry;
import org.tinylog.Logger;

import java.util.concurrent.CompletableFuture;

/**
 * Registry responsible for loading and managing all available {@link Reforge} instances.
 * <p></p>
 * This registry asynchronously loads reforges from configuration, then registers
 * them on the main thread to ensure thread safety with the Minestom server.
 */
public class ReforgeRegistry extends Registry<String, Reforge> {

    /**
     * Initializes the registry by loading all reforges from configuration.
     * <p></p>
     * The loading process occurs asynchronously to avoid blocking the main thread,
     * but registration happens on the main thread to ensure thread safety.
     */
    @Override
    public void init() {
        final long startTime = System.nanoTime(); // Benchmark start
        CompletableFuture.supplyAsync(() -> {
            ReforgeLoader loader = new ReforgeLoader();
            try {
                return loader.loadReforges();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load reforges", e);
            }
        }).thenAccept(reforges -> {
            Logger.info("Successfully loaded {} reforges", reforges.size());

            // Run registration on the main thread
            MinecraftServer.getSchedulerManager().buildTask(() -> {

                for (Reforge reforge : reforges) {
                    register(reforge.reforgeId(), reforge);
                }

                long endTime = System.nanoTime();
                long durationMs = (endTime - startTime) / 1_000_000;
                Logger.info("ReforgeRegistry initialization completed in {} ms", durationMs);
                lock();
            }).schedule();
        });
    }
}
