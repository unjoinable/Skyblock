package net.skyblock.item.definition;

import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;

/**
 * Represents an immutable Hypixel Skyblock item with its associated components.
 * The item's properties are stored in a {@link AttributeContainer}.
 *
 * @param itemId     Unique item identifier (e.g., "HYPERION").
 * @param attributes thread-safe container holding various {@link ItemAttribute} instances that define the item's properties.
 */
public record SkyblockItem(String itemId, AttributeContainer attributes) {
    public static final SkyblockItem AIR = new SkyblockItem("AIR", AttributeContainer.empty());
}