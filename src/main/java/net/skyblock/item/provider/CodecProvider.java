package net.skyblock.item.provider;

import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.codec.Codec;
import net.minestom.server.tag.Tag;
import net.skyblock.item.attribute.base.ItemAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Interface that provides access to codecs and their associated NBT tags.
 * This interface decouples the codec lookup functionality from specific registry implementations
 * and provides various helper methods for accessing codecs by different identifiers.
 */
public interface CodecProvider {
    /**
 * Retrieves the codec associated with the specified key, cast to the expected type.
 *
 * @param key the string identifier for the codec
 * @param <T> the type parameter for the codec
 * @return an Optional containing the codec if found, or an empty Optional if not present
 */
    <T> @NotNull Optional<Codec<T>> getCodec(@NotNull String key);

    /**
 * Retrieves the codec associated with the specified attribute class, cast to the expected type.
 *
 * @param attributeClass the attribute class for which to retrieve the codec
 * @param <T> the type produced by the codec
 * @param <A> the attribute class type
 * @return an Optional containing the codec if found, or empty if no codec is registered for the class
 */
    <T, A extends ItemAttribute> @NotNull Optional<Codec<T>> getCodecForClass(@NotNull Class<A> attributeClass);

    /**
 * Returns an unmodifiable map of all registered codecs keyed by their string identifiers.
 *
 * @return an unmodifiable map containing all registered codecs
 */
    @NotNull Map<String, Codec<?>> getCodecs();

    /**
 * Returns an unmodifiable set of all registered item attribute classes.
 *
 * @return an unmodifiable set containing all attribute classes managed by this provider
 */
    @NotNull Set<Class<? extends ItemAttribute>> getAttributeClasses();

    /**
 * Retrieves the cached NBT tag associated with the specified key.
 *
 * @param key the identifier for the desired NBT tag
 * @return an Optional containing the corresponding NBT tag if present, or empty if not found
 */
    @NotNull Optional<Tag<BinaryTag>> getTag(@NotNull String key);

    /**
 * Retrieves the NBT tag associated with the specified attribute class, if available.
 *
 * @param attributeClass the attribute class whose NBT tag is to be retrieved
 * @return an Optional containing the corresponding NBT tag, or empty if none exists
 */
    @NotNull Optional<Tag<BinaryTag>> getTagForClass(@NotNull Class<? extends ItemAttribute> attributeClass);

    /**
 * Returns an unmodifiable map of all cached NBT tags keyed by their string identifiers.
 *
 * @return an unmodifiable map containing all cached NBT tags
 */
    @NotNull Map<String, Tag<BinaryTag>> getTags();

    /**
 * Determines whether a codec is registered for the specified key.
 *
 * @param key the string identifier to check
 * @return true if a codec is registered for the key; false otherwise
 */
    boolean hasCodec(@NotNull String key);

    /**
 * Determines whether a codec is registered for the specified attribute class.
 *
 * @param attributeClass the attribute class to check for an associated codec
 * @return true if a codec exists for the given class; false otherwise
 */
    boolean hasCodecForClass(@NotNull Class<? extends ItemAttribute> attributeClass);

    /**
 * Retrieves the attribute class linked to the specified tag key, if present.
 *
 * @param key the tag key to look up
 * @return an Optional containing the associated attribute class, or empty if none exists
 */
    @NotNull Optional<Class<? extends ItemAttribute>> getAttributeClass(@NotNull String key);

    /**
 * Retrieves the tag key associated with the specified attribute class, if present.
 *
 * @param attributeClass the attribute class whose tag key is to be retrieved
 * @return an Optional containing the tag key if it exists, or an empty Optional otherwise
 */
    @NotNull Optional<String> getTagKey(@NotNull Class<? extends ItemAttribute> attributeClass);
}