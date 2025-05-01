package net.skyblock.item;

import com.google.gson.*;
import net.skyblock.Skyblock;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.registry.HandlerRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A dedicated loader class responsible for loading {@link SkyblockItem}s
 * from a JSON file in the resources' directory.
 */
public class SkyblockItemLoader {
    private static final String ITEM_FILE_PATH = "/skyblock_items.json";
    private static final String DEFAULT_ITEM_ID = "AIR";

    private final HandlerRegistry handlerRegistry;

    /**
     * Creates a new SkyblockItemLoader with a properly configured Gson instance.
     */
    public SkyblockItemLoader(@NotNull HandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    /**
     * Loads Skyblock items from the JSON file in the resources' directory.
     *
     * @return A list of {@link SkyblockItem} instances
     * @throws IllegalStateException if the file is missing, malformed, or if parsing fails
     */
    public List<SkyblockItem> loadItems() {
        Logger logger = Skyblock.getLogger();
        logger.info("Starting to load items from: {}", ITEM_FILE_PATH);

        // Load resource from a classpath instead of a file system
        InputStream inputStream = getClass().getResourceAsStream(ITEM_FILE_PATH);
        if (inputStream == null) {
            logger.error("Items file not found in resources: {}", ITEM_FILE_PATH);
            throw new IllegalStateException("Items file not found in resources: " + ITEM_FILE_PATH);
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            logger.info("Parsing JSON file...");
            List<SkyblockItem> items = parseItemsFromJson(reader);
            logger.info("Successfully loaded {} items", items.size());
            return items;
        } catch (Exception e) {
            logger.error("Failed to load Skyblock items: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to load Skyblock items from resources", e);
        }
    }

    /**
     * Parses the list of items from JSON.
     *
     * @param reader A reader for the item JSON file.
     * @return A list of {@link SkyblockItem} instances.
     * @throws IllegalStateException if parsing fails.
     */
    private List<SkyblockItem> parseItemsFromJson(InputStreamReader reader) {
        Logger logger = Skyblock.getLogger();
        JsonArray itemsArray;

        try {
            itemsArray = JsonParser.parseReader(reader).getAsJsonArray();
            logger.info("Found {} items in JSON file", itemsArray.size());
        } catch (JsonSyntaxException | IllegalStateException e) {
            logger.error("Failed to parse JSON: {}", e.getMessage());
            throw new IllegalStateException("Failed to parse items file - invalid JSON format", e);
        }

        List<SkyblockItem> items = new ArrayList<>();
        int itemIndex = 0;
        int successCount = 0;
        int failCount = 0;

        for (JsonElement itemElement : itemsArray) {
            itemIndex++;
            try {
                JsonObject itemObject = itemElement.getAsJsonObject();

                // Extract ID with fallback
                String id = DEFAULT_ITEM_ID;
                if (itemObject.has("id")) {
                    id = itemObject.get("id").getAsString();
                } else {
                    logger.warn("Item at index {} has no ID, using default: {}", itemIndex, DEFAULT_ITEM_ID);
                }

                // Create component container
                ComponentContainer components = new ComponentContainer();
                int componentCount = 0;

                // Process each property as a potential component
                for (Map.Entry<String, JsonElement> entry : itemObject.entrySet()) {
                    String componentId = entry.getKey();
                    JsonElement componentData = entry.getValue();

                    // Skip the ID field as it's not a component
                    if ("id".equals(componentId)) {
                        continue;
                    }

                    ItemComponentHandler<?> handler = handlerRegistry.getHandler(componentId);

                    if (handler != null) {
                        try {
                            ItemComponent component = handler.fromJson(componentData);
                            if (component != null) {
                                components = components.with(component);
                                componentCount++;
                                logger.debug("Added component '{}' to item {}", componentId, id);
                            } else {
                                logger.warn("Handler for '{}' returned null component for item {}", componentId, id);
                            }
                        } catch (Exception e) {
                            logger.warn("Failed to process component '{}' for item {}: {}",
                                    componentId, id, e.getMessage());
                        }
                    } else {
                        logger.debug("No handler found for property '{}' in item {}", componentId, id);
                    }
                }

                // Create the item and add to the list
                SkyblockItem item = new SkyblockItem(id, components);
                items.add(item);
                successCount++;
                logger.debug("Successfully loaded item {} with {} components", id, componentCount);

            } catch (Exception e) {
                failCount++;
                logger.error("Failed to process item at index {}: {}", itemIndex, e.getMessage());
            }
        }

        logger.info("Item loading complete: {} successful, {} failed", successCount, failCount);
        return items;
    }
}