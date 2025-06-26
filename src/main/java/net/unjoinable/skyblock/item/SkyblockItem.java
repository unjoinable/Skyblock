package net.unjoinable.skyblock.item;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;

public record SkyblockItem(ItemMetadata metadata, AttributeContainer attributes) {
    public static final Codec<SkyblockItem> CODEC = StructCodec.struct(
            "metadata", ItemMetadata.CODEC, SkyblockItem::metadata,
            "attributes", AttributeContainer.CODEC, SkyblockItem::attributes,
            SkyblockItem::new

    );
    public static SkyblockItem AIR = new SkyblockItem(ItemMetadata.DEFAULT, AttributeContainer.EMPTY);
}
