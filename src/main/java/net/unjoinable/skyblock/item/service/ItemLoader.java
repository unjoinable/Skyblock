package net.unjoinable.skyblock.item.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.utils.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ItemLoader {
    private static final String SKYBLOCK_ITEMS_PATH = "skyblock_items.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemLoader.class);

    private final ExecutorService executor;

    public ItemLoader() {
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "ItemLoader-Thread");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Loads all items from the JSON file in resources asynchronously
     * @return CompletableFuture containing List of all SkyblockItems, empty list if error occurs
     */
    public CompletableFuture<List<SkyblockItem>> loadItemsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.debug("Starting to load items on thread: {}", Thread.currentThread().getName());

                JsonObject root = FileUtils.loadJsonFromResource(SKYBLOCK_ITEMS_PATH);
                JsonArray items = root.getAsJsonArray("items");

                List<SkyblockItem> skyblockItems = new ArrayList<>();

                for (int i = 0; i < items.size(); i++) {
                    JsonObject itemJson = items.get(i).getAsJsonObject();
                    SkyblockItem item = loadItem(itemJson);
                    if (item != null) {
                        skyblockItems.add(item);
                    }
                }

                LOGGER.info("Loaded {} items on thread: {}", skyblockItems.size(), Thread.currentThread().getName());
                return skyblockItems;

            } catch (IOException e) {
                LOGGER.error("Error while reading skyblock_items.json from resources", e);
                return new ArrayList<>();
            }
        }, executor);
    }

    /**
     * Loads all items from the JSON file in resources synchronously (blocks until complete)
     * @return List of all SkyblockItems, empty list if error occurs
     */
    public List<SkyblockItem> loadItems() {
        try {
            return loadItemsAsync().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Item loading was interrupted", e);
            return new ArrayList<>();
        } catch (Exception e) {
            LOGGER.error("Error while loading items synchronously", e);
            return new ArrayList<>();
        } finally {
            shutdown();
        }
    }

    /**
     * Handles the deserialization of individual items
     * @param itemJson The JsonObject representing a single item
     * @return SkyblockItem or null if deserialization fails
     */
    private @Nullable SkyblockItem loadItem(JsonObject itemJson) {
        Result<SkyblockItem> result = SkyblockItem.CODEC.decode(Transcoder.JSON, itemJson);

        if (result instanceof Result.Ok(SkyblockItem item)) {
            return item;
        }
        return null;
    }

    /**
     * Shuts down the executor service. Call this when the ItemLoader is no longer needed.
     */
    public void shutdown() {
        executor.shutdown();
    }
}