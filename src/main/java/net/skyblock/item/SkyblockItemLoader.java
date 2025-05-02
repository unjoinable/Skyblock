package net.skyblock.item;

import com.google.gson.*;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.registry.HandlerRegistry;
import org.jetbrains.annotations.NotNull;

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
    private final HandlerRegistry handlerRegistry;

    public SkyblockItemLoader(@NotNull HandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    /**
     * Loads Skyblock items from the JSON file in resources
     */
    public List<SkyblockItem> loadItems() {
        InputStream inputStream = getClass().getResourceAsStream(ITEM_FILE_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("Items file not found: " + ITEM_FILE_PATH);
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return parseItemsFromJson(reader);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load Skyblock items", e);
        }
    }

    /**
     * Parses items from JSON
     */
    private List<SkyblockItem> parseItemsFromJson(InputStreamReader reader) {
        JsonArray itemsArray = JsonParser.parseReader(reader).getAsJsonArray();
        List<SkyblockItem> items = new ArrayList<>();

        for (JsonElement itemElement : itemsArray) {
            try {
                JsonObject itemObject = itemElement.getAsJsonObject();
                String id = itemObject.has("id") ? itemObject.get("id").getAsString() : DEFAULT_ITEM_ID;
                ComponentContainer components = parseComponents(itemObject);
                items.add(new SkyblockItem(id, components));
            } catch (Exception _) {} // ignored
        }

        return items;
    }

    /**
     * Extracts components from item JSON
     */
    private ComponentContainer parseComponents(JsonObject itemObject) {
        ComponentContainer components = new ComponentContainer();

        for (Map.Entry<String, JsonElement> entry : itemObject.entrySet()) {
            String componentId = entry.getKey();
            if ("id".equals(componentId)) continue;

            ItemComponentHandler<?> handler = handlerRegistry.getHandler(componentId);
            if (handler != null) {
                try {
                    ItemComponent component = handler.fromJson(entry.getValue());
                    if (component != null) {
                        components = components.with(component);
                    }
                } catch (Exception _) {}//ignored
            }
        }

        return components;
    }
}