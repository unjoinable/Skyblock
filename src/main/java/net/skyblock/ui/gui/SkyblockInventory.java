package net.skyblock.ui.gui;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.skyblock.player.SkyblockPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A Skyblock inventory implementation using Minestom GUI system.
 * Provides utility methods for building GUIs and handling click events.
 */
@SuppressWarnings("unused")
public class SkyblockInventory extends Inventory {

    // Static registry of all active inventories for event handling
    private static final Set<SkyblockInventory> ACTIVE_INVENTORIES = new ObjectOpenHashSet<>();

    // Cached frequently accessed values
    private final UUID uniqueId = UUID.randomUUID();
    private final Int2ObjectMap<BiConsumer<SkyblockPlayer, SkyblockInventory>> slotCallbacks = new Int2ObjectOpenHashMap<>();
    private Consumer<InventoryCloseEvent> closeCallback;

    private boolean cancelAllClicks = true;

    /**
     * Creates a new Skyblock inventory with the specified type and title.
     *
     * @param inventoryType The inventory type
     * @param title The inventory title
     */
    public SkyblockInventory(InventoryType inventoryType, String title) {
        super(inventoryType, title);
        ACTIVE_INVENTORIES.add(this);
    }

    /**
     * Gets an unmodifiable view of all active inventories.
     *
     * @return Set of all active SkyblockInventory instances
     */
    public static Set<SkyblockInventory> getActiveInventories() {
        return Collections.unmodifiableSet(ACTIVE_INVENTORIES);
    }

    /**
     * Handles the inventory click event and calls the appropriate callbacks.
     *
     * @param event The inventory pre-click event
     * @return True if the event was handled, false otherwise
     */
    public static boolean handleInventoryClick(InventoryPreClickEvent event) {
        Inventory inventory = (Inventory) event.getInventory();

        if (inventory instanceof SkyblockInventory skyblockInventory) {
            Player player = event.getPlayer();

            if (!(player instanceof SkyblockPlayer skyblockPlayer)) {
                event.setCancelled(true);
                return true;
            }

            if (skyblockInventory.shouldCancelAllClicks()) {
                event.setCancelled(true);
            }

            // Execute the callback for this slot if one exists
            BiConsumer<SkyblockPlayer, SkyblockInventory> callback =
                    skyblockInventory.getCallback(event.getSlot());

            if (callback != null) {
                callback.accept(skyblockPlayer, skyblockInventory);
                return true;
            }

            return skyblockInventory.shouldCancelAllClicks();
        }

        return false;
    }

    /**
     * Handles inventory close events and removes the inventory from the registry.
     *
     * @param event The inventory close event
     */
    public static void handleInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = (Inventory) event.getInventory();

        if (inventory instanceof SkyblockInventory skyblockInventory) {
            ACTIVE_INVENTORIES.remove(skyblockInventory);

            if (skyblockInventory.closeCallback != null) {
                skyblockInventory.closeCallback.accept(event);
            }
        }
    }

    /**
     * Sets an item in the inventory and assigns a callback for when it's clicked.
     *
     * @param slot The slot to set
     * @param item The item to set
     * @param callback The callback to call when the item is clicked
     * @return This inventory for chaining
     */
    public SkyblockInventory setItem(int slot, ItemStack item, BiConsumer<SkyblockPlayer, SkyblockInventory> callback) {
        setItemStack(slot, item);

        if (callback != null) {
            slotCallbacks.put(slot, callback);
        } else {
            slotCallbacks.remove(slot);
        }

        return this;
    }

    /**
     * Sets an item in the inventory without a click callback.
     *
     * @param slot The slot to set
     * @param item The item to set
     * @return This inventory for chaining
     */
    public SkyblockInventory setItem(int slot, ItemStack item) {
        return setItem(slot, item, null);
    }

    /**
     * Fills the entire inventory with the specified item.
     *
     * @param item The item to fill with
     * @return This inventory for chaining
     */
    public SkyblockInventory fill(ItemStack item) {
        for (int i = 0; i < getSize(); i++) {
            setItem(i, item);
        }
        return this;
    }

    /**
     * Sets the border of the inventory to the specified item.
     *
     * @param item The item to use for the border
     * @return This inventory for chaining
     */
    public SkyblockInventory setBorder(ItemStack item) {
        int size = getSize();
        int rows = size / 9;

        for (int i = 0; i < 9; i++) {
            setItem(i, item);
            setItem(size - 9 + i, item);
        }

        for (int i = 1; i < rows - 1; i++) {
            setItem(i * 9, item);
            setItem(i * 9 + 8, item);
        }

        return this;
    }

    /**
     * Fills a rectangular area of the inventory with the specified item.
     *
     * @param startSlot The starting slot (top-left)
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param item The item to fill with
     * @return This inventory for chaining
     */
    public SkyblockInventory fillRect(int startSlot, int width, int height, ItemStack item) {
        int startRow = startSlot / 9;
        int startCol = startSlot % 9;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int slot = (startRow + row) * 9 + (startCol + col);
                if (slot < getSize()) {
                    setItem(slot, item);
                }
            }
        }

        return this;
    }

    /**
     * Sets a callback for when the inventory is closed.
     *
     * @param closeCallback The callback to call when the inventory is closed
     * @return This inventory for chaining
     */
    public SkyblockInventory setCloseCallback(Consumer<InventoryCloseEvent> closeCallback) {
        this.closeCallback = closeCallback;
        return this;
    }

    /**
     * Sets whether all clicks should be cancelled by default.
     *
     * @param cancel True to cancel all clicks, false otherwise
     * @return This inventory for chaining
     */
    public SkyblockInventory setCancelAllClicks(boolean cancel) {
        this.cancelAllClicks = cancel;
        return this;
    }

    /**
     * Gets the callback for the specified slot.
     *
     * @param slot The slot to get the callback for
     * @return The callback for the slot, or null if none exists
     */
    public BiConsumer<SkyblockPlayer, SkyblockInventory> getCallback(int slot) {
        return slotCallbacks.get(slot);
    }

    /**
     * Checks if all clicks should be cancelled by default.
     *
     * @return True if all clicks should be cancelled, false otherwise
     */
    public boolean shouldCancelAllClicks() {
        return cancelAllClicks;
    }

    /**
     * Opens the inventory for the specified player.
     *
     * @param player The player to open the inventory for
     */
    public void open(SkyblockPlayer player) {
        player.openInventory(this);
    }

    /**
     * Gets the unique ID of this inventory.
     *
     * @return The unique ID
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SkyblockInventory other)) return false;
        return uniqueId.equals(other.uniqueId);
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }
}