package net.unjoinable.skyblock.item.attribute.impls;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.item.attribute.traits.LoreAttribute;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.utils.MiniString;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Attribute that provides description text for items in their lore.
 */
public record DescriptionAttribute(List<Component> description) implements LoreAttribute {
    public static final Key KEY = Key.key("attribute:description");
    public static final Codec<DescriptionAttribute> CODEC = StructCodec.struct(
            "description", MiniString.CODEC.list(), DescriptionAttribute::description,
            DescriptionAttribute::new
    );

    public DescriptionAttribute(List<Component> description) {
        this.description = new ArrayList<>(description);
    }

    @Override
    public List<Component> loreLines(@Nullable SkyblockPlayer player, AttributeContainer container, ItemMetadata metadata) {
        return new ArrayList<>(description);
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public Codec<? extends ItemAttribute> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }
}
