package net.unjoinable.skyblock.item.attribute.traits;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.skyblock.utils.NamespaceId;

import java.util.Optional;

/**
 * The base interface for all item attributes.
 * This interface provides codec functionality for serialization and deserialization.
 */
public interface ItemAttribute {

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

    default Codec<ItemAttribute> safeCodec() {
        //noinspection unchecked
        return (Codec<ItemAttribute>) codec();
    }

    /**
     * Encodes this attribute to an object of the specified type using the provided transcoder.
     *
     * <p>This method attempts to serialize the current attribute instance into the target
     * type {@code T}. If the encoding fails for any reason, an empty Optional is returned.</p>
     *
     * @param <T> the target type to encode to
     * @param transcoder the transcoder to use for the encoding operation
     * @return an Optional containing the encoded object if successful, empty otherwise
     * @throws ClassCastException if the codec cannot be cast to handle ItemAttribute
     */
    default <T> Optional<T> asObject(Transcoder<T> transcoder) {
        try {
            //noinspection unchecked
            Result<T> result = ((Codec<ItemAttribute>) codec()).encode(transcoder, this);

            if (result instanceof Result.Ok<T>(T value)) {
                return Optional.of(value);
            }
        } catch (ClassCastException _) {/* Return Empty Optional */}

        return Optional.empty();
    }
}
