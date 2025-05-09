package net.skyblock.item.attribute.impl;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.attribute.base.StackAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an attribute that sets the material type of an item.
 * This fundamental attribute determines the base item type (such as
 * diamond sword, golden apple, etc.) that will be used when the item
 * is created.
 */
public record MaterialAttribute(@NotNull Material material) implements StackAttribute, JsonAttribute {
    public static final String ID = "material";
    public static final Codec<MaterialAttribute> CODEC = StructCodec.struct(
            "material", Material.CODEC, MaterialAttribute::material,
            MaterialAttribute::new
    );

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyToBuilder(ItemStack.@NotNull Builder builder, @NotNull AttributeContainer container) {
        builder.material(material);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }
}