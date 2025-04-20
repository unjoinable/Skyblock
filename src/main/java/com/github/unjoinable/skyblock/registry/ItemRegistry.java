package com.github.unjoinable.skyblock.registry;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.component.adapters.*;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.github.unjoinable.skyblock.item.enums.Rarity;
import com.github.unjoinable.skyblock.stats.StatProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * A registry responsible for loading and registering {@link SkyblockItem}s
 * from a JSON file during application startup.
 */
public class ItemRegistry extends Registry<String, SkyblockItem> {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatProfile.class, new StatProfileAdapter())
            .registerTypeAdapter(Rarity.class, new RarityAdapter())
            .registerTypeAdapter(ItemCategory.class, new ItemCategoryAdapter())
            .registerTypeAdapter(Material.class, new MaterialAdapter())
            .registerTypeAdapter(Component.class, new ComponentAdapter())
            .create();

    private static final File ITEM_FILE = new File("skyblock_items.json");

    /**
     * Initializes the item registry by loading and registering all Skyblock items
     * defined in the {@code skyblock_items.json} file.
     *
     * @throws RuntimeException if the file is missing, malformed, or if item registration fails.
     */
    @Override
    public void init() {
        validateFileExists();

        System.out.println("Loading items from: " + ITEM_FILE.getAbsolutePath());

        try (FileReader reader = new FileReader(ITEM_FILE)) {
            System.out.println("Parsing JSON...");
            List<SkyblockItem.Builder> items = parseItemsFromJson(reader);

            System.out.println("Successfully parsed " + items.size() + " items");
            registerItems(items);

        } catch (Exception e) {
            System.err.println("Critical error during item registration: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load and register Skyblock items", e);
        }
    }

    /**
     * Validates that the JSON file exists before parsing.
     *
     * @throws RuntimeException if the file does not exist.
     */
    private void validateFileExists() {
        if (!ITEM_FILE.exists()) {
            throw new RuntimeException("Items file not found at: " + ITEM_FILE.getAbsolutePath());
        }
    }

    /**
     * Parses the list of item builders from JSON.
     *
     * @param reader A reader for the item JSON file.
     * @return A list of {@link SkyblockItem.Builder} instances.
     * @throws Exception if parsing fails.
     */
    private List<SkyblockItem.Builder> parseItemsFromJson(FileReader reader) throws Exception {
        Type listType = new TypeToken<List<SkyblockItem.Builder>>() {}.getType();
        List<SkyblockItem.Builder> items = gson.fromJson(reader, listType);

        if (items == null) {
            throw new RuntimeException("Failed to parse items file — JSON is null");
        }

        return items;
    }

    /**
     * Attempts to register each item from the list of item builders.
     *
     * @param items A list of {@link SkyblockItem.Builder} objects to build and register.
     */
    private void registerItems(List<SkyblockItem.Builder> items) {
        int successCount = 0;
        int failureCount = 0;

        for (SkyblockItem.Builder builder : items) {
            try {
                SkyblockItem item = builder.build();
                String itemId = item.itemId() != null ? item.itemId() : "unknown";
                System.out.println("Registering item: " + itemId);
                register(itemId, item);
                successCount++;
            } catch (Exception e) {
                System.err.println("Failed to register item: " + e.getMessage());
                e.printStackTrace();
                failureCount++;
            }
        }

        System.out.println("Item registration complete — Success: " + successCount + ", Failed: " + failureCount);
    }
}
