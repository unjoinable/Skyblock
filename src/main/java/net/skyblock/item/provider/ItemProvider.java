package net.skyblock.item.provider;

import net.skyblock.item.definition.SkyblockItem;
import org.jetbrains.annotations.NotNull;

/**
 * Provides access to {@link SkyblockItem} definitions by their item ID.
 * <p>
 * If an item with the specified ID is not found, this provider will return
 * a fallback {@link SkyblockItem} representing AIR (i.e., an empty or null-equivalent item).
 */
public interface ItemProvider {

    /**
     * Returns the {@link SkyblockItem} associated with the given item ID.
     * If the item ID is unknown or not registered, this method returns a fallback item representing AIR.
     *
     * @param itemId the ID of the item to retrieve
     * @return the {@link SkyblockItem} associated with the ID, or AIR if not found
     */
    @NotNull SkyblockItem getItem(@NotNull String itemId);
}
