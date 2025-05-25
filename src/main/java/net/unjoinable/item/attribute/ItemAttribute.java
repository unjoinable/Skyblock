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
     * Returns the codec used for encoding and decoding this attribute.
     *
     * <p>The codec defines the serialization format and rules for converting
     * this attribute to and from various representations.</p>
     *
     * @return the codec for this attribute, never null
     */
    @NotNull Codec<? extends ItemAttribute> codec();
}
