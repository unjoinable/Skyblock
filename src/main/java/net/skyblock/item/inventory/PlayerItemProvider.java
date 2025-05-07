package net.skyblock.item.inventory;

import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.io.ItemProcessor;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides access to a player's equipped items across all slots
 */
public class PlayerItemProvider {
    private final SkyblockPlayer player;
    private final ItemProcessor processor;
    private final Map<ItemSlot, SkyblockItem> cachedItems;

    /**
     * Create a new PlayerItemProvider
     * @param player The player
     * @param processor The item processor
     */
    public PlayerItemProvider(@NotNull SkyblockPlayer player, @NotNull ItemProcessor processor) {
        this.player = player;
        this.processor = processor;
        this.cachedItems = new HashMap<>();
    }

    /**
     * Gets the item in a specific slot
     * @param slot The slot
     * @return The item, or null if none
     */
    public @Nullable SkyblockItem getItem(@NotNull ItemSlot slot) {
        return cachedItems.computeIfAbsent(slot, s -> s.getItem(player, processor));
    }

    /**
     * Gets all armor items (helmet, chestplate, leggings, boots)
     * @return Map of armor slots to their items
     */
    public Map<ItemSlot, SkyblockItem> getArmorItems() {
        return getItemsForSlots(
                VanillaItemSlot.HELMET,
                VanillaItemSlot.CHESTPLATE,
                VanillaItemSlot.LEGGINGS,
                VanillaItemSlot.BOOTS
        );
    }

    /**
     * Gets the player's main hand weapon
     * @return The weapon item, or null if none
     */
    public @Nullable SkyblockItem getWeapon() {
        return getItem(VanillaItemSlot.MAIN_HAND);
    }

    /**
     * Gets items from multiple slots
     * @param slots The slots to check
     * @return Map of slots to their items
     */
    public Map<ItemSlot, SkyblockItem> getItemsForSlots(@NotNull ItemSlot... slots) {
        Map<ItemSlot, SkyblockItem> result = new HashMap<>();
        for (ItemSlot slot : slots) {
            SkyblockItem item = getItem(slot);
            if (item != null) {
                result.put(slot, item);
            }
        }
        return result;
    }

    /**
     * Checks if a slot has an item
     * @param slot The slot to check
     * @return True if an item is present
     */
    public boolean hasItemInSlot(@NotNull ItemSlot slot) {
        return getItem(slot) != null;
    }

    /**
     * Invalidates cached items
     */
    public void invalidateCache() {
        cachedItems.clear();
    }
}