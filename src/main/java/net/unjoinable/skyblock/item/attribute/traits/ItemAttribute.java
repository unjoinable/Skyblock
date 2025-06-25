package net.unjoinable.skyblock.item.attribute.traits;

import net.minestom.server.codec.Codec;
import net.unjoinable.skyblock.utility.NamespaceId;

/**
 * The base interface for all item attributes.
 * This interface provides codec functionality for serialization and deserialization.
 */
public sealed interface ItemAttribute permits CodecAttribute, LoreAttribute, StatModifierAttribute {

    NamespaceId id();

    /**
     * Returns the codec used for encoding and decoding this attribute.
     *
     * <p>The codec defines the serialization format and rules for converting
     * this attribute to and from various representations.</p>
     *
     * @return the codec for this attribute, never null
     */
    Codec<? extends ItemAttribute> codec();
}
