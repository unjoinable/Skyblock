package net.skyblock.item.attribute.base;

import net.minestom.server.codec.Codec;
import org.jetbrains.annotations.NotNull;

/**
 * Interface used to identify classes that represent custom
 * attributes applicable to items.
 * <p>
 * This interface provides identification for attribute types
 * via a unique string ID. Classes implementing this interface
 * are processed by attribute management systems to apply their
 * defined effects or properties.
 * </p>
 */
public interface ItemAttribute {
    /**
     * Returns the unique identifier for this attribute type.
     *
     * @return A string ID that uniquely identifies this attribute type
     */
    @NotNull String id();

    /**
     * Returns the codec used to serialize and deserialize this attribute's data.
     * The codec defines how the attribute's value is converted to and from NBT or JSON format.
     *
     * @return The codec for this attribute's data type
     */
    @NotNull Codec<? extends ItemAttribute> getCodec();
}