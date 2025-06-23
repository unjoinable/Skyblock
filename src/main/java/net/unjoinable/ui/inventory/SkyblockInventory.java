package net.unjoinable.ui.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A custom inventory implementation for Skyblock with advanced item management.
 * Supports dynamic items that can be updated, playback items with click interactions,
 * and stealable/non-stealable item controls.
 */
public abstract class SkyblockInventory extends Inventory {
    /**
     * Allowed inventory types to prevent misuse
     */
    private static final Set<InventoryType> ALLOWED_TYPES = Set.of(
            InventoryType.CHEST_1_ROW,
            InventoryType.CHEST_2_ROW,
            InventoryType.CHEST_3_ROW,
            InventoryType.CHEST_4_ROW,
            InventoryType.CHEST_5_ROW,
            InventoryType.CHEST_6_ROW
    );

    private final Map<Integer, DynamicItem> dynamicItems = new HashMap<>();
    private final Map<Integer, PlaybackItem> playbackItems = new HashMap<>();
    private final Set<Integer> stealableSlots = new HashSet<>();
    private boolean defaultStealable = false;

    /**
     * Represents an item that can be dynamically updated
     */
    private record DynamicItem(@NotNull Supplier<ItemStack> itemSupplier) {}

    /**
     * Represents an item with a specific click interaction
     */
    private record PlaybackItem(@NotNull ItemStack item, @NotNull Consumer<InventoryPreClickEvent> clickHandler) {}

    /**
     * Creates a new SkyblockInventory with type validation
     *
     * @param inventoryType The type of inventory to create
     * @param title The title of the inventory
     * @throws IllegalArgumentException if an invalid inventory type is used
     */
    protected SkyblockInventory(@NotNull InventoryType inventoryType, @NotNull Component title) {
        super(inventoryType, title);

        // Validate inventory type
        if (!ALLOWED_TYPES.contains(inventoryType)) {
            throw new IllegalArgumentException("Invalid inventory type: " + inventoryType);
        }
    }

    /**
     * Adds a dynamic item that can be updated later
     *
     * @param slot The slot to place the dynamic item
     * @param itemSupplier A supplier that provides the current version of the item
     */
    public void setDynamicItem(int slot, @NotNull Supplier<ItemStack> itemSupplier) {
        validateSlot(slot);
        dynamicItems.put(slot, new DynamicItem(itemSupplier));
        setItemStack(slot, itemSupplier.get());
    }

    /**
     * Adds a playback item with a click interaction
     * Playback items are always non-stealable by default
     *
     * @param slot The slot to place the playback item
     * @param item The ItemStack to display
     * @param clickHandler The handler for click interactions
     */
    public void setPlaybackItem(
            int slot,
            @NotNull ItemStack item,
            @NotNull Consumer<InventoryPreClickEvent> clickHandler
    ) {
        validateSlot(slot);
        playbackItems.put(slot, new PlaybackItem(item, clickHandler));
        setItemStack(slot, item);
        // Playback items are non-stealable by default
        setSlotNonStealable(slot);
    }

    /**
     * Sets the default stealability for all items in the inventory
     *
     * @param stealable Whether items should be stealable by default
     */
    public void setDefaultStealable(boolean stealable) {
        this.defaultStealable = stealable;
    }

    /**
     * Makes all items in the inventory non-stealable
     */
    public void makeAllNonStealable() {
        stealableSlots.clear();
        setDefaultStealable(false);
    }

    /**
     * Makes all items in the inventory stealable
     */
    public void makeAllStealable() {
        stealableSlots.clear();
        for (int i = 0; i < getSize(); i++) {
            stealableSlots.add(i);
        }
        setDefaultStealable(true);
    }

    /**
     * Sets a specific slot as stealable
     *
     * @param slot The slot to make stealable
     */
    public void setSlotStealable(int slot) {
        validateSlot(slot);
        stealableSlots.add(slot);
    }

    /**
     * Sets a specific slot as non-stealable
     *
     * @param slot The slot to make non-stealable
     */
    public void setSlotNonStealable(int slot) {
        validateSlot(slot);
        stealableSlots.remove(slot);
    }

    /**
     * Sets multiple slots as stealable
     *
     * @param slots The slots to make stealable
     */
    public void setSlotsStealable(int... slots) {
        for (int slot : slots) {
            setSlotStealable(slot);
        }
    }

    /**
     * Sets multiple slots as non-stealable
     *
     * @param slots The slots to make non-stealable
     */
    public void setSlotsNonStealable(int... slots) {
        for (int slot : slots) {
            setSlotNonStealable(slot);
        }
    }

    /**
     * Checks if a slot is stealable
     *
     * @param slot The slot to check
     * @return true if the slot is stealable, false otherwise
     */
    public boolean isSlotStealable(int slot) {
        validateSlot(slot);

        // If the slot is explicitly marked as stealable, return true
        if (stealableSlots.contains(slot)) {
            return true;
        }

        // If it's a playback item, it's never stealable (except if explicitly set)
        if (playbackItems.containsKey(slot)) {
            return false;
        }

        // Otherwise, use the default stealability
        return defaultStealable;
    }

    /**
     * Updates all dynamic items in the inventory
     */
    public void updateDynamicItems() {
        dynamicItems.forEach((slot, dynamicItem) ->
                setItemStack(slot, dynamicItem.itemSupplier().get())
        );
    }

    /**
     * Handles click events for both playback items and stealability
     *
     * @param event The inventory click event
     */
    public void handleClick(InventoryPreClickEvent event) {
        int slot = event.getSlot();

        // Check for playback item first - these always execute their handler
        PlaybackItem playbackItem = playbackItems.get(slot);
        if (playbackItem != null) {
            playbackItem.clickHandler().accept(event);
            // Playback items are always cancelled unless explicitly made stealable
            if (!stealableSlots.contains(slot)) {
                event.setCancelled(true);
            }
            return;
        }

        // Check if the slot is stealable
        if (!isSlotStealable(slot)) {
            event.setCancelled(true);
        }
    }

    /**
     * Validates that a slot is within the inventory bounds
     *
     * @param slot The slot to validate
     * @throws IllegalArgumentException if the slot is out of bounds
     */
    private void validateSlot(int slot) {
        if (slot < 0 || slot >= getSize()) {
            throw new IllegalArgumentException("Slot " + slot + " is out of bounds for inventory size " + getSize());
        }
    }

    /**
     * Fills the entire inventory with a specific item
     *
     * @param item The item to fill the inventory with
     */
    public void fill(@NotNull ItemStack item) {
        for (int i = 0; i < getSize(); i++) {
            setItemStack(i, item);
        }
    }

    /**
     * Creates a border around the center of the inventory with maximum possible radius
     *
     * @param item The item to use for the border
     */
    public void border(@NotNull ItemStack item) {
        int centerSlot = calculateCenterSlot();
        int maxRadius = calculateMaxRadius(centerSlot);
        border(centerSlot, maxRadius, item);
    }

    /**
     * Creates a border around the center of the inventory with the specified radius
     *
     * @param radius The radius of the border (must be <= max radius)
     * @param item The item to use for the border
     * @throws IllegalArgumentException if radius exceeds maximum possible radius
     */
    public void border(int radius, @NotNull ItemStack item) {
        int centerSlot = calculateCenterSlot();
        int maxRadius = calculateMaxRadius(centerSlot);

        if (radius > maxRadius) {
            throw new IllegalArgumentException("Radius " + radius + " exceeds maximum radius " + maxRadius + " for this inventory");
        }

        border(centerSlot, radius, item);
    }

    /**
     * Creates a border around the specified slot with the given radius
     *
     * @param centerSlot The slot to consider as center
     * @param radius The radius of the border
     * @param item The item to use for the border
     */
    public void border(int centerSlot, int radius, @NotNull ItemStack item) {
        validateSlot(centerSlot);

        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }

        if (radius == 0) {
            setItemStack(centerSlot, item);
            return;
        }

        int centerRow = centerSlot / 9;
        int centerCol = centerSlot % 9;

        // Create border by checking each slot in the radius square
        for (int row = centerRow - radius; row <= centerRow + radius; row++) {
            for (int col = centerCol - radius; col <= centerCol + radius; col++) {
                // Skip if out of bounds
                if (row < 0 || row >= getSize() / 9 || col < 0 || col >= 9) {
                    continue;
                }

                // Check if this position is on the border (not inside)
                boolean isOnBorder = (row == centerRow - radius || row == centerRow + radius ||
                        col == centerCol - radius || col == centerCol + radius);

                if (isOnBorder) {
                    int slot = row * 9 + col;
                    setItemStack(slot, item);
                }
            }
        }
    }

    /**
     * Calculates the maximum possible radius for a border centered at the given slot
     *
     * @param centerSlot The center slot
     * @return The maximum radius that fits within the inventory bounds
     */
    private int calculateMaxRadius(int centerSlot) {
        int centerRow = centerSlot / 9;
        int centerCol = centerSlot % 9;
        int totalRows = getSize() / 9;

        // Calculate maximum distance to any edge
        int maxRadiusDown = totalRows - 1 - centerRow;
        int maxRadiusRight = 8 - centerCol;

        // Return the minimum of all distances (the limiting factor)
        return Math.min(Math.min(centerRow, maxRadiusDown),
                Math.min(centerCol, maxRadiusRight));
    }

    /**
     * Calculates the center slot of the inventory
     *
     * @return The index of the center slot
     */
    public int calculateCenterSlot() {
        int rows = getSize() / 9;
        int centerRow = rows / 2;
        return (centerRow * 9) + 4;
    }
}