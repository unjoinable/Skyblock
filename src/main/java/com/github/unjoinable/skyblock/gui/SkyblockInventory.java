package com.github.unjoinable.skyblock.gui;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import org.jetbrains.annotations.NotNull;

public class SkyblockInventory extends Inventory {
    public static final Tag<String> BUTTON_TAG = Tag.String("button");

    /**
     * Constructs a new SkyblockInventory.
     *
     * @param inventoryType the type of inventory to create
     * @param title the title of the inventory
     */
    public SkyblockInventory(@NotNull InventoryType inventoryType, @NotNull Component title) {
        super(inventoryType, title);
    }

    /**
     * Fills the entire inventory with the specified item.
     *
     * @param item the ItemStack to fill the inventory with
     */
    public void fill(@NotNull ItemStack item) {
        for (int i = 0; i < getInventoryType().getSize() ; i++) {
            setItemStack(i, item);
        }
    }

    /**
     * Adds a clickable item to the inventory at the specified slot.
     *
     * @param clickable the ClickableItem to add
     * @param player the SkyblockPlayer for whom the item is being added
     * @param slot the inventory slot to place the item in
     */
    public void addClickableItem(@NotNull ClickableItem clickable, @NotNull SkyblockPlayer player, int slot) {
        ItemStack itemStack = clickable.item().apply(player);
        itemStack = itemStack.withTag(BUTTON_TAG, clickable.id().toString());
        setItemStack(slot, itemStack);
    }

    /**
     * Clears the entire inventory, setting all slots to air.
     */
    public void clear() {
        for (int i = 0; i < getInventoryType().getSize() ; i++) {
            setItemStack(i, ItemStack.AIR);
        }
    }

    /**
     * Creates an item with hidden tooltips and an empty name.
     *
     * @param material the Material to use for the filler item
     * @return an ItemStack configured as a filler item
     */
    public static ItemStack createFillerItem(Material material) {
        ItemStack.Builder builder = ItemStack.of(material).builder();
        builder.set(ItemComponent.HIDE_TOOLTIP, Unit.INSTANCE);
        builder.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        builder.set(ItemComponent.CUSTOM_NAME, Component.empty());
        return builder.build();
    }
}
