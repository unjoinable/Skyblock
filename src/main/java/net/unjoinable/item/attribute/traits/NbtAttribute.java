package net.unjoinable.item.attribute.traits;

import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.item.attribute.ItemAttribute;

import java.util.Optional;

/**
 * Represents an extension of {@link ItemAttribute} that enables NBT (Named Binary Tag) serialization.
 * This interface provides methods for converting attributes to NBT tags and vice versa.
 */
public interface NbtAttribute extends CodecAttribute {

    /**
     * Converts the attribute into an optional {@link BinaryTag} representation using the default NBT {@link Transcoder}.
     *
     * @return An {@link Optional} containing the serialized NBT tag, or empty if not applicable.
     */
    default Optional<BinaryTag> asObject() {
        return asObject(Transcoder.NBT);
    }
}