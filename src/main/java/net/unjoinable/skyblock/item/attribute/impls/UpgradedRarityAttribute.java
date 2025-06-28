package net.unjoinable.skyblock.item.attribute.impls;

import net.kyori.adventure.key.Key;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.attribute.traits.NbtAttribute;
import org.jetbrains.annotations.NotNull;

public record UpgradedRarityAttribute(boolean isUpgraded) implements NbtAttribute {
    public static final Key KEY = Key.key("attribute:upgraded_rarity");
    public static final Codec<UpgradedRarityAttribute> CODEC = StructCodec.struct(
            "isUpgraded", Codec.BOOLEAN, UpgradedRarityAttribute::isUpgraded,
            UpgradedRarityAttribute::new
    );

    @Override
    public @NotNull Key key() {
        return KEY;
    }

    @Override
    public Codec<UpgradedRarityAttribute> codec() {
        return CODEC;
    }
}
