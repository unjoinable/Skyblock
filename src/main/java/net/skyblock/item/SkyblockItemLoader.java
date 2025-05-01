package net.skyblock.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.skyblock.Skyblock;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * A dedicated loader class responsible for loading {@link SkyblockItem}s
 * from a JSON file in the resources' directory.
 */
public class SkyblockItemLoader {

    private static final String ITEM_FILE_PATH = "/skyblock_items.json";

    private final Gson gson;

    /**
     * Creates a new SkyblockItemLoader with a properly configured Gson instance.
     */
    public SkyblockItemLoader() {
        this.gson = new GsonBuilder()
                .create();
    }

    /**
     * Loads Skyblock items from the JSON file in the resources' directory.
     *
     * @return A list of {@link SkyblockItem} instances
     * @throws IllegalStateException if the file is missing, malformed, or if parsing fails
     */
    public List<SkyblockItem> loadItems() {
        Logger logger = Skyblock.getLogger();
        logger.info("Loading items from: " + ITEM_FILE_PATH);

        // Load resource from a classpath instead of a file system
        InputStream inputStream = getClass().getResourceAsStream(ITEM_FILE_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("Items file not found in resources: " + ITEM_FILE_PATH);
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            logger.info("Parsing JSON...");
            return parseItemsFromJson(reader);
        } catch (Exception e) {
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
        Type listType = new TypeToken<List<SkyblockItem>>() {}.getType();
        List<SkyblockItem> items = gson.fromJson(reader, listType);

        if (items == null) {
            throw new IllegalStateException("Failed to parse items file — JSON is null");
        }

        return items;
    }
}
