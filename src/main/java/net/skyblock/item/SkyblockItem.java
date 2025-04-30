package net.skyblock.item;

import net.skyblock.item.component.Component;
import net.skyblock.item.component.ComponentContainer;

/**
 * Represents an immutable Hypixel Skyblock item with its associated components.
 * The item's properties are stored in a {@link ComponentContainer}.
 *
 * @param itemId     Unique item identifier (e.g., "HYPERION").
 * @param components A thread-safe container holding various {@link Component} instances that define the item's properties.
 */
public record SkyblockItem(String itemId, ComponentContainer components) {
    public static final SkyblockItem AIR = new SkyblockItem("AIR", null);

    /**
     * Creates a new builder for constructing a SkyblockItem.
     *
     * @return A new builder instance
     */
    public static SkyblockItemBuilder builder() {
        return new SkyblockItemBuilder();
    }
}