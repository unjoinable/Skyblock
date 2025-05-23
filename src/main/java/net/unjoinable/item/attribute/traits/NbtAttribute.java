package net.unjoinable.item.attribute.traits;

import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.item.attribute.ItemAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Extension of {@link ItemAttribute} with NBT (Named Binary Tag) serialization capabilities.
 * This interface provides methods for converting attributes to NBT tags and vice versa.
 */
public interface NbtAttribute extends ItemAttribute {
    /**
     * Converts this attribute to an NBT tag.
     *
     * @return An Optional containing the NBT representation if successful, or empty if encoding failed
     */
    default @NotNull Optional<BinaryTag> asNbtTag() {
        final Result<BinaryTag> result = asNbtCodec().encode(Transcoder.NBT, this);
        return result instanceof Result.Ok<BinaryTag>(BinaryTag value) ? Optional.of(value) : Optional.empty();
    }

    /**
     * Attempts to create an attribute of the specified type from an NBT tag.
     *
     * @param tag The NBT tag to decode
     * @return An Optional containing the decoded attribute if successful and of the correct type,
     *         or empty if decoding failed or produced an attribute of the wrong type
     */
    default @NotNull Optional<NbtAttribute> fromNbtTag(@NotNull BinaryTag tag) {
        final Result<NbtAttribute> result = asNbtCodec().decode(Transcoder.NBT, tag);
        return result instanceof Result.Ok(NbtAttribute value) ? Optional.of(value) : Optional.empty();
    }

    private Codec<NbtAttribute> asNbtCodec() {
        //noinspection unchecked
        return (Codec<NbtAttribute>) codec();
    }
}
