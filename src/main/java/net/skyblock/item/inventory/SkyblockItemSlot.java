package net.skyblock.item.inventory;

import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.io.ItemProcessor;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Example implementation for custom slots like talismans, accessories, etc.
 */
public class SkyblockItemSlot implements ItemSlot {
    private final String name;
    private final SlotItemGetter itemGetter;

    /**
     * Create a new custom item slot
     * @param name The slot name
     * @param itemGetter Function to retrieve items from this slot
     */
    public SkyblockItemSlot(@NotNull String name, @NotNull SlotItemGetter itemGetter) {
        this.name = name;
        this.itemGetter = itemGetter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor) {
        return itemGetter.getItem(player, processor);
    }

    /**
     * Functional interface for retrieving items from slots
     */
    @FunctionalInterface
    public interface SlotItemGetter {
        @Nullable SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor);
    }
}
