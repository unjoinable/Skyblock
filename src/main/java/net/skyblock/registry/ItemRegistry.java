package net.skyblock.registry;

import net.skyblock.item.SkyblockItem;
import net.skyblock.item.SkyblockItemLoader;

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
        try {
            // Use the dedicated loader class to load items
            SkyblockItemLoader loader = new SkyblockItemLoader();
            var items = loader.loadItems();

            System.out.println("Successfully loaded " + items.size() + " items");
            registerItems(items);

        } catch (Exception e) {
            System.err.println("Critical error during item registration: " + e.getMessage());
            throw new RuntimeException("Failed to load and register Skyblock items", e);
        }
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
                System.err.println("Failed to register item: " + e.getMessage());
                failureCount++;
            }
        }

        System.out.println("Item registration complete — Success: " + successCount + ", Failed: " + failureCount);
    }
}