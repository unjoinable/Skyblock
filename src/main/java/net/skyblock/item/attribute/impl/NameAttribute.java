package net.skyblock.item.attribute.impl;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.item.ItemStack;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.AttributeResolver;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.attribute.base.StackAttribute;
import net.skyblock.item.enums.Rarity;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * Represents an attribute that sets a custom display name for an item.
 * This attribute overrides the default item name with a custom string
 * that will be displayed in the game interface.
 */
public record NameAttribute(@NotNull String name) implements StackAttribute, JsonAttribute {
    public static final String ID = "name";
    public static final Codec<NameAttribute> CODEC = StructCodec.struct(
            "name", Codec.STRING, NameAttribute::name,
            NameAttribute::new
    );

    /**
     * Sets the custom display name of the item in the builder, styled with the item's rarity color and without italic decoration.
     *
     * @param builder the item stack builder to apply the custom name to
     * @param container the attribute container used to resolve the item's rarity
     */
    @Override
    public void applyToBuilder(ItemStack.@NotNull Builder builder, @NotNull AttributeContainer container) {
        Rarity rarity = AttributeResolver.resolveRarity(container);
        builder.customName(text(name, rarity.getColor()).decoration(ITALIC, false));
    }

    /**
     * Returns the identifier string for this attribute.
     *
     * @return the attribute ID "name"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * Returns the codec used for serializing and deserializing this attribute.
     *
     * @return the codec for this attribute
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }
}