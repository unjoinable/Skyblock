package net.unjoinable.player.ui.inventory;

import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.service.ItemProcessor;
import net.unjoinable.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;
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
    @Nullable SkyblockItem getItem(@NotNull SkyblockPlayer player, @NotNull ItemProcessor processor);
}
