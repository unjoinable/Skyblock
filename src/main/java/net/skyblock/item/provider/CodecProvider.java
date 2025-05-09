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
     * Gets the codec for the given string key and casts it to the expected type.
     *
     * @param key The key to look up
     * @param <T> The expected codec type
     * @return Optional containing the codec, or empty if not found
     */
    <T> @NotNull Optional<Codec<T>> getCodec(@NotNull String key);

    /**
     * Gets the codec for the given attribute class and casts it to the expected type.
     *
     * @param attributeClass The attribute class to look up
     * @param <T> The expected codec type
     * @param <A> The attribute type
     * @return Optional containing the codec, or empty if not found
     */
    <T, A extends ItemAttribute> @NotNull Optional<Codec<T>> getCodecForClass(@NotNull Class<A> attributeClass);

    /**
     * Gets all registered codecs.
     *
     * @return An unmodifiable view of all registered codecs
     */
    @NotNull Map<String, Codec<?>> getCodecs();

    /**
     * Gets all registered attribute classes.
     *
     * @return An unmodifiable set of all registered attribute classes
     */
    @NotNull Set<Class<? extends ItemAttribute>> getAttributeClasses();

    /**
     * Gets the cached NBT tag for the given key.
     *
     * @param key The key to look up
     * @return Optional containing the NBT tag, or empty if not found
     */
    @NotNull Optional<Tag<BinaryTag>> getTag(@NotNull String key);

    /**
     * Gets the NBT tag for the given attribute class.
     *
     * @param attributeClass The attribute class to look up
     * @return Optional containing the NBT tag, or empty if not found
     */
    @NotNull Optional<Tag<BinaryTag>> getTagForClass(@NotNull Class<? extends ItemAttribute> attributeClass);

    /**
     * Gets all cached NBT tags.
     *
     * @return An unmodifiable view of all cached tags
     */
    @NotNull Map<String, Tag<BinaryTag>> getTags();

    /**
     * Checks if a codec exists for the given string key.
     *
     * @param key The key to check
     * @return true if a codec exists for the key, false otherwise
     */
    boolean hasCodec(@NotNull String key);

    /**
     * Checks if a codec exists for the given attribute class.
     *
     * @param attributeClass The attribute class to check
     * @return true if a codec exists for the class, false otherwise
     */
    boolean hasCodecForClass(@NotNull Class<? extends ItemAttribute> attributeClass);

    /**
     * Gets the attribute class associated with a tag key.
     *
     * @param key The tag key to look up
     * @return The associated attribute class, if any
     */
    @NotNull Optional<Class<? extends ItemAttribute>> getAttributeClass(@NotNull String key);

    /**
     * Gets the tag key associated with an attribute class.
     *
     * @param attributeClass The attribute class to look up
     * @return The associated tag key, if any
     */
    @NotNull Optional<String> getTagKey(@NotNull Class<? extends ItemAttribute> attributeClass);
}