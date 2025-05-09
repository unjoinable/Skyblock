package net.skyblock.item.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.provider.CodecProvider;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Loads {@link SkyblockItem}s from JSON
 */
public class SkyblockItemLoader {
    private static final String ITEM_FILE_PATH = "/skyblock_items.json";
    private static final String DEFAULT_ITEM_ID = "AIR";
    private final CodecProvider codecProvider;

    /**
     * Constructs a SkyblockItemLoader with the specified codec provider for attribute decoding.
     *
     * @param codecProvider the provider used to obtain codecs for decoding item attributes
     */
    public SkyblockItemLoader(@NotNull CodecProvider codecProvider) {
        this.codecProvider = codecProvider;
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
     * Parses a JSON array of item definitions into a list of {@link SkyblockItem} objects.
     *
     * @param reader an InputStreamReader for the JSON resource containing item definitions
     * @return a list of successfully parsed SkyblockItem instances; items that fail to parse are skipped
     */
    private List<SkyblockItem> parseItemsFromJson(InputStreamReader reader) {
        JsonArray itemsArray = JsonParser.parseReader(reader).getAsJsonArray();
        List<SkyblockItem> items = new ArrayList<>();

        for (int i = 0; i < itemsArray.size(); i++) {
            try {
                JsonObject itemObject = itemsArray.get(i).getAsJsonObject();
                SkyblockItem item = parseItem(itemObject);
                items.add(item);

            } catch (Exception e) {
                Logger.warn(e, "Error parsing item at index {}, skipping", i);
            }
        }

        return items;
    }

    /**
     * Parses a JSON object into a {@code SkyblockItem} with decoded attributes.
     *
     * @param itemObject the JSON object representing the item
     * @return a {@code SkyblockItem} with attributes decoded from the JSON, or {@code null} if parsing fails
     */
    private SkyblockItem parseItem(JsonObject itemObject) {
        String id = extractItemId(itemObject);
        AttributeContainer.Builder builder = AttributeContainer.builder();

        codecProvider.getAttributeClasses().stream()
                .filter(JsonAttribute.class::isAssignableFrom)
                .map(codecProvider::getCodecForClass)
                .flatMap(Optional::stream)
                .map(codec -> codec.decode(Transcoder.JSON, itemObject))
                .forEach(result -> {
                    System.out.println(result);
                    if (result instanceof Result.Ok(JsonAttribute attribute)) {
                        builder.with(attribute);
                    }
                });

        return new SkyblockItem(id, builder.build());
    }
    /**
     * Returns the item ID from the given JSON object, or the default ID if the "id" field is missing.
     *
     * @param itemObject the JSON object representing an item
     * @return the extracted item ID, or the default ID if not present
     */
    private String extractItemId(JsonObject itemObject) {
        return itemObject.has("id") ? itemObject.get("id").getAsString() : DEFAULT_ITEM_ID;
    }
}