package net.unjoinable.item.attribute;

import net.minestom.server.codec.Codec;
import net.unjoinable.utility.NamespaceId;
import org.jetbrains.annotations.NotNull;

/**
 * The base interface for all item attributes.
 * This interface provides codec functionality for serialization and deserialization.
 */
public interface ItemAttribute {

    @NotNull NamespaceId id();

    /**
     * Returns the codec for this attribute type.
     *
     * @return A codec capable of encoding/decoding this attribute type
     */
    @NotNull Codec<? extends ItemAttribute> codec();
}
