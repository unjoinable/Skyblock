package net.skyblock.item.attribute.impl;

import net.kyori.adventure.util.RGBLike;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.attribute.base.StackAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an attribute that applies a specific color to dyeable armor items.
 * This attribute uses Minestom's {@link DataComponents#DYED_COLOR} component to
 * apply the color effect, which is typically used for leather armor.
 */
public record ArmorColorAttribute(@NotNull RGBLike color) implements StackAttribute, JsonAttribute {
    public static final String ID = "armor_color";
    public static final Codec<ArmorColorAttribute> CODEC = StructCodec.struct(
            "armor_color", Color.CODEC, ArmorColorAttribute::color,
            ArmorColorAttribute::new
    );

    /**
     * Applies the stored color to the item stack builder as the dyed color component.
     *
     * This sets the {@code DataComponents.DYED_COLOR} of the item being built, enabling dyeable armor to display the specified color.
     */
    @Override
    public void applyToBuilder(ItemStack.@NotNull Builder builder, @NotNull AttributeContainer container) {
        builder.set(DataComponents.DYED_COLOR, color);
    }

    /****
     * Returns the unique identifier for this attribute.
     *
     * @return the string "armor_color"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * Returns the codec used for serializing and deserializing this armor color attribute.
     *
     * @return the codec for this attribute
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }
}