package net.unjoinable.skyblock.item.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.skyblock.item.SkyblockItem;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public final class ItemLoader {
    private static final File SKYBLOCK_ITEMS = new File("skyblock_items.json");
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemLoader.class);

    /**
     * Loads all items from the JSON file
     * @return List of all SkyblockItems, empty list if error occurs
     */
    public List<SkyblockItem> loadItems() {
        try (FileReader reader = new FileReader(SKYBLOCK_ITEMS)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray items = root.getAsJsonArray("items");

            List<SkyblockItem> skyblockItems = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                JsonObject itemJson = items.get(i).getAsJsonObject();
                SkyblockItem item = loadItem(itemJson);
                if (item != null) {
                    skyblockItems.add(item);
                }
            }
            LOGGER.info("Loaded {} items", skyblockItems.size());
            return skyblockItems;

        } catch (Exception e) {
            LOGGER.error("Error while reading skyblock_items.json", e);
            return new ArrayList<>();
        }
    }

    /**
     * Override this method to handle the deserialization of individual items
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
}