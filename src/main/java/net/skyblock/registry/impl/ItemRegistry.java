package net.skyblock.registry.impl;

import net.minestom.server.MinecraftServer;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.io.SkyblockItemLoader;
import net.skyblock.item.provider.CodecProvider;
import net.skyblock.item.provider.ItemProvider;
import net.skyblock.registry.base.Registry;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.util.concurrent.CompletableFuture;

/**
 * A registry responsible for storing and retrieving {@link SkyblockItem}s.
 * This registry follows the Single Responsibility Principle by focusing only
 * on registration and lookup functionality.
 */
public class ItemRegistry extends Registry<String, SkyblockItem> implements ItemProvider {
    private final CodecProvider codecProvider;

    /**
     * Constructs an ItemRegistry with the specified CodecProvider for item loading.
     *
     * @param codecProvider the CodecProvider used to load SkyblockItem instances
     */
    public ItemRegistry(@NotNull CodecProvider codecProvider) {
        this.codecProvider = codecProvider;
    }

    /**
     * Asynchronously loads Skyblock items and registers them in the registry.
     *
     * Initiates loading of items using the configured CodecProvider, then registers them on the main server thread.
     * Logs the number of loaded items and the total initialization time. If an error occurs during loading or registration,
     * logs a critical error and aborts the process.
     */
    @Override
    public void init() {
        final long startTime = System.nanoTime(); // Benchmark start

        CompletableFuture.supplyAsync(() -> {
            SkyblockItemLoader loader = new SkyblockItemLoader(this.codecProvider);
            return loader.loadItems();
        }).thenAccept(items -> {
            Logger.info("Successfully loaded {} items", items.size());

            // Run registration on the main thread
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                registerItems(items);
                long endTime = System.nanoTime();
                long durationMs = (endTime - startTime) / 1_000_000;
                Logger.info("ItemRegistry initialization completed in {} ms", durationMs);
                lock();
            }).schedule();
        }).exceptionally(ex -> {
            Logger.error("Critical error during item registration", ex);
            return null;
        });
    }


    /**
     * Attempts to register each item from the list of items.
     *
     * @param items A list of {@link SkyblockItem} objects to register.
     */
    private void registerItems(java.util.List<SkyblockItem> items) {
        int successCount = 0;
        int failureCount = 0;

        for (SkyblockItem item : items) {
            try {
                String itemId = item.itemId() != null ? item.itemId() : "unknown";
                register(itemId, item);
                successCount++;
            } catch (Exception e) {
                Logger.error("Failed to register item: {}", e.getMessage());
                failureCount++;
            }
        }

        Logger.info("Item registration complete — Success: {}, Failed: {}", successCount, failureCount);
    }

    /**
     * Returns the {@link SkyblockItem} associated with the given item ID.
     * If the item ID is unknown or not registered, this method returns a fallback item representing AIR.
     *
     * @param itemId the ID of the item to retrieve
     * @return the {@link SkyblockItem} associated with the ID, or AIR if not found
     */
    @Override
    public @NotNull SkyblockItem getItem(@NotNull String itemId) {
        SkyblockItem item = get(itemId);
        if (item == null) {
            return SkyblockItem.AIR;
        }

        return item;
    }
}