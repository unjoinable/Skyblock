package net.skyblock.item.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.registry.impl.HandlerRegistry;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads {@link SkyblockItem}s from JSON
 */
public class SkyblockItemLoader {
    private static final String ITEM_FILE_PATH = "/skyblock_items.json";
    private static final String DEFAULT_ITEM_ID = "AIR";
    private final HandlerRegistry handlers;

    public SkyblockItemLoader(@NotNull HandlerRegistry handlers) {
        this.handlers = handlers;
    }

    /**
     * Loads Skyblock items from the JSON file in resources
     * @return List of successfully loaded SkyblockItems
     */
    public @NotNull List<SkyblockItem> loadItems() {
        try (InputStreamReader reader = openItemsFile()) {
            if (reader == null) {
                return List.of();
            }
            return parseItemsFromJson(reader);
        } catch (Exception e) {
            Logger.error(e, "Failed to load Skyblock items");
            return List.of();
        }
    }

    /**
     * Opens and returns a reader for the items file
     * @return InputStreamReader or null if file not found
     */
    private InputStreamReader openItemsFile() {
        InputStream inputStream = getClass().getResourceAsStream(ITEM_FILE_PATH);
        if (inputStream == null) {
            Logger.error("Items file not found: {}", ITEM_FILE_PATH);
            return null;
        }
        return new InputStreamReader(inputStream);
    }

    /**
     * Parses items from JSON
     */
    private List<SkyblockItem> parseItemsFromJson(InputStreamReader reader) {
        JsonArray itemsArray = JsonParser.parseReader(reader).getAsJsonArray();
        List<SkyblockItem> items = new ArrayList<>();

        for (int i = 0; i < itemsArray.size(); i++) {
            try {
                JsonObject itemObject = itemsArray.get(i).getAsJsonObject();
                SkyblockItem item = parseItem(itemObject, i);
                items.add(item);

            } catch (Exception e) {
                Logger.warn(e, "Error parsing item at index {}, skipping", i);
            }
        }

        return items;
    }

    /**
     * Parses a single item from its JSON object
     * @param itemObject The JSON object representing the item
     * @param index The index of the item in the array (for logging)
     * @return The parsed SkyblockItem or null if parsing failed
     */
    private SkyblockItem parseItem(JsonObject itemObject, int index) {
        String id = extractItemId(itemObject);
        ComponentContainer components = parseComponents(itemObject, id);

        if (components.isEmpty()) {
            Logger.warn("Item '{}' at index {} has no valid components", id, index);
        }

        return new SkyblockItem(id, components);
    }

    /**
     * Extracts the item ID from the JSON object or returns the default ID
     */
    private String extractItemId(JsonObject itemObject) {
        return itemObject.has("id") ? itemObject.get("id").getAsString() : DEFAULT_ITEM_ID;
    }

    /**
     * Extracts components from item JSON
     * @param itemObject The JSON object containing component data
     * @param itemId The ID of the item (for logging)
     * @return ComponentContainer with all successfully parsed components
     */
    private ComponentContainer parseComponents(JsonObject itemObject, String itemId) {
        ComponentContainer components = new ComponentContainer();

        for (Map.Entry<String, JsonElement> entry : itemObject.entrySet()) {
            String componentId = entry.getKey();
            if ("id".equals(componentId)) continue;

            try {
                ItemComponent component = parseComponent(componentId, entry.getValue());
                if (component != null) {
                    components = components.with(component);
                }
            } catch (Exception e) {
                Logger.warn(e, "Failed to parse component '{}' for item '{}', skipping component",
                        componentId, itemId);
            }
        }

        return components;
    }

    /**
     * Parses a single component using its handler
     * @param componentId The ID of the component
     * @param jsonElement The JSON element containing the component data
     * @return The parsed ItemComponent or null if no handler exists
     */
    private ItemComponent parseComponent(String componentId, JsonElement jsonElement) {
        ItemComponentHandler<?> handler = handlers.getHandler(componentId);
        if (handler == null) {
            Logger.debug("No handler found for component '{}'", componentId);
            return null;
        }

        return handler.fromJson(jsonElement);
    }
}