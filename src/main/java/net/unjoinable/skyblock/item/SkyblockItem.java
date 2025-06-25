package net.unjoinable.skyblock.item;

import net.unjoinable.skyblock.item.attribute.AttributeContainer;

public record SkyblockItem(ItemMetadata metadata, AttributeContainer attributes) {
    public static SkyblockItem AIR = new SkyblockItem(ItemMetadata.DEFAULT, AttributeContainer.EMPTY);
}
