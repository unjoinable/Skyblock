package net.skyblock.registry;

import net.minestom.server.MinecraftServer;
import net.skyblock.Skyblock;
import net.skyblock.item.SkyblockItem;
import net.skyblock.item.SkyblockItemLoader;

import java.util.concurrent.CompletableFuture;

/**
 * A registry responsible for storing and retrieving {@link SkyblockItem}s.
 * This registry follows the Single Responsibility Principle by focusing only
 * on registration and lookup functionality.
 */
public class ItemRegistry extends Registry<String, SkyblockItem> {

    /**
     * Initializes the item registry by loading items from the JSON file
     * and registering them.
     */
    @Override
    public void init() {
        final long startTime = System.nanoTime(); // Benchmark start

        CompletableFuture.supplyAsync(() -> {
            SkyblockItemLoader loader = new SkyblockItemLoader();
            try {
                return loader.loadItems();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load Skyblock items", e);
            }
        }).thenAccept(items -> {
            Skyblock.getLogger().info("Successfully loaded {} items", items.size());

            // Run registration on the main thread
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                registerItems(items);
                long endTime = System.nanoTime();
                long durationMs = (endTime - startTime) / 1_000_000;
                Skyblock.getLogger().info("ItemRegistry initialization completed in {} ms", durationMs);
            }).schedule();
        }).exceptionally(ex -> {
            Skyblock.getLogger().error("Critical error during item registration", ex);
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
                Skyblock.getLogger().error("Failed to register item: {}", e.getMessage());
                failureCount++;
            }
        }

        Skyblock.getLogger().info("Item registration complete — Success: {}, Failed: {}", successCount, failureCount);
    }
}