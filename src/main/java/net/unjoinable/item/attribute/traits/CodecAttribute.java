package net.unjoinable.item.attribute.traits;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.item.attribute.ItemAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A trait interface for {@link ItemAttribute} implementations that can be serialized
 * and deserialized using Minestom's codec system.
 *
 * <p>This interface provides a standardized way to handle encoding and decoding of
 * item attributes, allowing them to be converted to and from various object types
 * using transcoders.</p>
 *
 * <p>Implementing classes must provide a codec that defines how the attribute
 * should be serialized and deserialized.</p>
 *
 * @see ItemAttribute
 * @see Codec
 * @see Transcoder
 */
public interface CodecAttribute extends ItemAttribute {

    /**
     * Returns the codec used for encoding and decoding this attribute.
     *
     * <p>The codec defines the serialization format and rules for converting
     * this attribute to and from various representations.</p>
     *
     * @return the codec for this attribute, never null
     */
    @NotNull Codec<? extends ItemAttribute> codec();

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

    /**
     * Decodes an object of type {@code T} into an ItemAttribute using the provided transcoder.
     *
     * <p>This method attempts to deserialize the provided object into an ItemAttribute
     * instance using this attribute's codec. If the decoding fails for any reason,
     * an empty Optional is returned.</p>
     *
     * @param <T> the type of the object to decode from
     * @param transcoder the transcoder to use for the decoding operation
     * @param object the object to decode into an ItemAttribute
     * @return an Optional containing the decoded ItemAttribute if successful, empty otherwise
     */
    default <T> Optional<ItemAttribute> fromObject(Transcoder<T> transcoder, T object) {
        Result<? extends ItemAttribute> result = codec().decode(transcoder, object);

        if (result instanceof Result.Ok(ItemAttribute value)) {
            return Optional.of(value);
        }

        return Optional.empty();
    }
}