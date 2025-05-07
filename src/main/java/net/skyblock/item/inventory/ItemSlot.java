package net.skyblock.item.inventory;

import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.io.ItemProcessor;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Represents any equipment slot that can hold a SkyblockItem
 */
public interface ItemSlot {
    /**
     * Gets the name of this slot
     * @return The slot name
     */
    String getName();

    /**
     * Retrieves the item in this slot for the given player
     * @param player The player
     * @param processor The item processor
     * @return The SkyblockItem in this slot, or null if none
     */
    @Nullable SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor);
}