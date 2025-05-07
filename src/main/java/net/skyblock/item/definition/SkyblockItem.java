package net.skyblock.item.definition;

import net.skyblock.item.component.ItemComponents;
import net.skyblock.item.component.ItemComponent;

/**
 * Represents an immutable Hypixel Skyblock item with its associated components.
 * The item's properties are stored in a {@link ItemComponents}.
 *
 * @param itemId     Unique item identifier (e.g., "HYPERION").
 * @param components A thread-safe container holding various {@link ItemComponent} instances that define the item's properties.
 */
public record SkyblockItem(String itemId, ItemComponents components) {
    public static final SkyblockItem AIR = new SkyblockItem("AIR", new ItemComponents());
}