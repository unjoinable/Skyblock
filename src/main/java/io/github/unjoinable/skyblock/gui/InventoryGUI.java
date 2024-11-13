package io.github.unjoinable.skyblock.gui;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryGUI {
    private String title;
    private InventoryType inventoryType;
    private final List<ItemStack> contents;
    private final UUID uuid;

    //statics
    public static final Tag<String> BUTTON_TAG = Tag.String("button");
    private static final Map<SkyblockPlayer, InventoryGUI> existingGuis = new ConcurrentHashMap<>();

    public InventoryGUI(@NotNull String title, @NotNull InventoryType inventoryType) {
        this.title = title;
        this.inventoryType = inventoryType;
        this.contents = new ArrayList<>();
        this.uuid = UUID.randomUUID();
    }

    public void fill(@NotNull ItemStack item) {
        contents.clear();
        for (int i = 0; i < inventoryType.getSize() ; i++) {
            contents.add(item);
        }
    }

    public void clear() {
        contents.clear();
    }

    public void addItem(@NotNull ItemStack item, byte slot) {
        contents.set(slot, item);
    }

    public void addButton(@NotNull Button button, byte slot) {

    }

    public void clearSlot(byte slot) {
        contents.set(slot, ItemStack.AIR);
    }

    public @NotNull Inventory build() {
        Inventory inventory = new Inventory(inventoryType, title);
        for (int i = 0; i < inventoryType.getSize() ; i++) {
            ItemStack item = contents.get(i);
            if (item == null) item = ItemStack.AIR;
            inventory.setItemStack(i, item);
        }
        return inventory;
    }

    public static Map<SkyblockPlayer, InventoryGUI> getExistingGuis() {
        return existingGuis;
    }

    public static @NotNull InventoryGUI getGUIForPlayer(@NotNull SkyblockPlayer player) {
        return existingGuis.get(player);
    }

}
