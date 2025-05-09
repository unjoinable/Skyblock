package net.skyblock.item.attribute.impl;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;

public record ItemCategoryAttribute(@NotNull ItemCategory category) implements JsonAttribute {
    public static final String ID = "item_category";
    public static final Codec<ItemCategoryAttribute> CODEC = StructCodec.struct(
            "category", Codec.Enum(ItemCategory.class), ItemCategoryAttribute::category,
            ItemCategoryAttribute::new
    );

    /**
     * Returns the unique identifier for this attribute type.
     *
     * @return the string "item_category"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * Returns the codec used for serializing and deserializing this item attribute.
     *
     * @return the codec for this attribute type
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }
}