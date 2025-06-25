package net.unjoinable.skyblock.item;

import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import org.jetbrains.annotations.NotNull;

public record SkyblockItem(@NotNull ItemMetadata metadata, @NotNull AttributeContainer attributes) {
    public static SkyblockItem AIR = new SkyblockItem(ItemMetadata.DEFAULT, AttributeContainer.EMPTY);
}
