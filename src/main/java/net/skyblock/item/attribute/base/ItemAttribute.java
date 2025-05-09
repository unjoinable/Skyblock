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
     * Retrieves the unique string identifier for this item attribute type.
     *
     * @return the non-null unique ID representing this attribute type
     */
    @NotNull String id();

    /**
     * Retrieves the codec responsible for serializing and deserializing this attribute's data.
     *
     * @return a non-null codec for converting the attribute to and from formats such as NBT or JSON
     */
    @NotNull Codec<? extends ItemAttribute> getCodec();
}