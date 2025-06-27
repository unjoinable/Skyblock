package net.unjoinable.skyblock.item.attribute.impls;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.attribute.traits.NbtAttribute;
import net.unjoinable.skyblock.utils.NamespaceId;


public record UpgradedRarityAttribute(boolean isUpgraded) implements NbtAttribute {
    public static final NamespaceId ID = new NamespaceId("attribute", "upgraded_rarity");
    public static final Codec<UpgradedRarityAttribute> CODEC = StructCodec.struct(
            "isUpgraded", Codec.BOOLEAN, UpgradedRarityAttribute::isUpgraded,
            UpgradedRarityAttribute::new
    );

    @Override
    public NamespaceId id() {
        return ID;
    }

    @Override
    public Codec<UpgradedRarityAttribute> codec() {
        return CODEC;
    }
}
