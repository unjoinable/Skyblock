package net.unjoinable.item.attribute.impls;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.item.attribute.traits.CodecAttribute;
import net.unjoinable.item.attribute.traits.NbtAttribute;
import net.unjoinable.utility.NamespaceId;
import org.jetbrains.annotations.NotNull;

public record UpgradedRarityAttribute(boolean isUpgraded) implements NbtAttribute, CodecAttribute {
    public static final NamespaceId ID = new NamespaceId("attribute", "upgraded_rarity");
    public static final Codec<UpgradedRarityAttribute> CODEC = StructCodec.struct(
            "isUpgraded", Codec.BOOLEAN, UpgradedRarityAttribute::isUpgraded,
            UpgradedRarityAttribute::new
    );

    @Override
    public @NotNull NamespaceId id() {
        return ID;
    }

    @Override
    public @NotNull Codec<UpgradedRarityAttribute> codec() {
        return CODEC;
    }
}
