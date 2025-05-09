package net.skyblock.registry.impl;

import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.codec.Codec;
import net.minestom.server.tag.Tag;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.impl.*;
import net.skyblock.item.provider.CodecProvider;
import net.skyblock.registry.base.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A concrete registry implementation that uses Class<? extends ItemAttribute> keys and Minestom Codec values.
 * This registry is specifically designed for Attributes and implements the CodecProvider interface.
 * It maintains a separate map for String-based tags to facilitate serialization.
 */
public class AttributeCodecRegistry extends Registry<Class<? extends ItemAttribute>, Codec<?>> implements CodecProvider {
    private final Map<String, Tag<BinaryTag>> cachedTags = new HashMap<>();
    private final Map<String, Class<? extends ItemAttribute>> stringToClass = new HashMap<>();
    private final Map<Class<? extends ItemAttribute>, String> classToString = new HashMap<>();

    /**
     * Associates an attribute class with a codec and a string tag key for NBT serialization.
     *
     * Registers the provided codec for the specified attribute class and tag key, updating all internal mappings.
     *
     * @param attributeClass the attribute class to register
     * @param tagKey the string key used for NBT serialization
     * @param codec the codec to associate with the attribute class
     * @throws IllegalStateException if the registry is locked
     * @throws IllegalArgumentException if the attribute class or tag key is already registered
     */
    public void registerAttribute(@NotNull Class<? extends ItemAttribute> attributeClass, @NotNull String tagKey, @NotNull Codec<?> codec) {
        Tag<BinaryTag> tag = Tag.NBT(tagKey);
        cachedTags.put(tagKey, tag);
        stringToClass.put(tagKey, attributeClass);
        classToString.put(attributeClass, tagKey);
        super.register(attributeClass, codec);
    }

    /**
     * Registers a codec for the given attribute class using an auto-generated tag key derived from the class name.
     *
     * @param attributeClass the attribute class to register
     * @param codec the codec to associate with the attribute class
     * @throws IllegalStateException if the registry is locked
     * @throws IllegalArgumentException if the attribute class is already registered
     */
    @Override
    public void register(@NotNull Class<? extends ItemAttribute> attributeClass, @NotNull Codec<?> codec) {
        String tagKey = generateTagKey(attributeClass);
        registerAttribute(attributeClass, tagKey, codec);
    }

    /**
     * Registers a codec for the specified attribute class using a custom tag key, or generates one from the class name if none is provided.
     *
     * @param attributeClass the attribute class to associate with the codec
     * @param codec the codec to register for the attribute class
     * @param customTagKey the tag key to use for registration, or null to auto-generate from the class name
     * @param <A> the type of the item attribute
     */
    public <A extends ItemAttribute> void registerCodec(@NotNull Class<A> attributeClass,
                                                        @NotNull Codec<?> codec,
                                                        String customTagKey) {
        String tagKey = customTagKey != null ? customTagKey : generateTagKey(attributeClass);
        registerAttribute(attributeClass, tagKey, codec);
    }

    /**
     * Registers a codec for the specified attribute class using an automatically generated tag key.
     *
     * The tag key is derived from the attribute class name by converting it to lowercase and removing the "attribute" suffix.
     *
     * @param attributeClass the attribute class to associate with the codec
     * @param codec the codec to register for the attribute class
     * @param <A> the type of the attribute
     */
    public <A extends ItemAttribute> void registerCodec(@NotNull Class<A> attributeClass,
                                                        @NotNull Codec<?> codec) {
        registerCodec(attributeClass, codec, null);
    }

    /**
     * Generates a tag key string from the given class by converting its simple name to lowercase and removing the substring "attribute".
     *
     * @param clazz the class for which to generate a tag key
     * @return the generated tag key string
     */
    private String generateTagKey(@NotNull Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase().replace("attribute", "");
    }

    /**
     * Retrieves the attribute class associated with the specified tag key.
     *
     * @param tagKey the string tag key used to identify the attribute class
     * @return an {@code Optional} containing the attribute class if found, or empty if not registered
     */
    @Override
    public @NotNull Optional<Class<? extends ItemAttribute>> getAttributeClass(@NotNull String tagKey) {
        return Optional.ofNullable(stringToClass.get(tagKey));
    }

    /**
     * Retrieves the tag key string associated with the specified attribute class, if present.
     *
     * @param attributeClass the attribute class to look up
     * @return an {@code Optional} containing the tag key if registered, or empty if not found
     */
    @Override
    public @NotNull Optional<String> getTagKey(@NotNull Class<? extends ItemAttribute> attributeClass) {
        return Optional.ofNullable(classToString.get(attributeClass));
    }

    /**
     * Retrieves the NBT tag associated with the specified tag key, if present.
     *
     * @param key the string tag key for which to retrieve the NBT tag
     * @return an {@code Optional} containing the corresponding {@code Tag<BinaryTag>}, or empty if not found
     */
    @Override
    public @NotNull Optional<Tag<BinaryTag>> getTag(@NotNull String key) {
        return Optional.ofNullable(cachedTags.get(key));
    }

    /**
     * Retrieves the NBT tag associated with the specified attribute class, if present.
     *
     * @param attributeClass the attribute class to look up
     * @return an {@code Optional} containing the corresponding {@code Tag<BinaryTag>} if found, or empty if not registered
     */
    @Override
    public @NotNull Optional<Tag<BinaryTag>> getTagForClass(@NotNull Class<? extends ItemAttribute> attributeClass) {
        return getTagKey(attributeClass).flatMap(this::getTag);
    }

    /**
     * Retrieves the codec associated with the specified tag key, if present.
     *
     * @param key the tag key identifying the attribute
     * @return an {@code Optional} containing the codec for the tag key, or empty if not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull Optional<Codec<T>> getCodec(@NotNull String key) {
        return getAttributeClass(key).map(this::get).map(codec -> (Codec<T>) codec);
    }

    /**
     * Retrieves the codec associated with the specified attribute class, if present.
     *
     * @param attributeClass the attribute class for which to retrieve the codec
     * @return an {@code Optional} containing the codec if registered, or empty if not found
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, A extends ItemAttribute> @NotNull Optional<Codec<T>> getCodecForClass(@NotNull Class<A> attributeClass) {
        return Optional.ofNullable((Codec<T>) get(attributeClass));
    }

    /**
     * Returns an unmodifiable map of all registered tag keys to their corresponding NBT tags.
     *
     * @return an unmodifiable map of tag keys to {@code Tag<BinaryTag>} instances
     */
    @Override
    public @NotNull Map<String, Tag<BinaryTag>> getTags() {
        return Collections.unmodifiableMap(cachedTags);
    }

    /**
     * Returns an unmodifiable map of tag keys to their associated codecs for all registered item attributes.
     *
     * @return an unmodifiable map where each key is a tag string and each value is the corresponding attribute codec
     */
    @Override
    public @NotNull Map<String, Codec<?>> getCodecs() {
        Map<String, Codec<?>> result = new HashMap<>();
        keyToValue.forEach((attributeClass, codec) -> {
            String tagKey = classToString.get(attributeClass);
            if (tagKey != null) {
                result.put(tagKey, codec);
            }
        });
        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns an unmodifiable set of all registered attribute classes in the registry.
     *
     * @return a set containing all attribute classes currently registered
     */
    @Override
    public @NotNull Set<Class<? extends ItemAttribute>> getAttributeClasses() {
        return Set.copyOf(keyToValue.keySet());
    }

    /**
     * Checks if a codec is registered for the specified tag key.
     *
     * @param key the tag key to check
     * @return true if a codec is registered for the given tag key, false otherwise
     */
    @Override
    public boolean hasCodec(@NotNull String key) {
        return stringToClass.containsKey(key) && containsKey(stringToClass.get(key));
    }

    /**
     * Checks if a codec is registered for the specified attribute class.
     *
     * @param attributeClass the attribute class to check
     * @return true if a codec is registered for the given attribute class, false otherwise
     */
    @Override
    public boolean hasCodecForClass(@NotNull Class<? extends ItemAttribute> attributeClass) {
        return containsKey(attributeClass);
    }

    /**
     * Registers the default set of item attribute codecs and locks the registry to prevent further modifications.
     */
    @Override
    public void init() {
        registerAttribute(RarityAttribute.class, RarityAttribute.ID, RarityAttribute.CODEC);
        registerAttribute(PotatoBookAttribute.class, PotatoBookAttribute.ID, PotatoBookAttribute.CODEC);
        registerAttribute(ArmorColorAttribute.class, ArmorColorAttribute.ID, ArmorColorAttribute.CODEC);
        registerAttribute(DescriptionAttribute.class, DescriptionAttribute.ID, DescriptionAttribute.CODEC);
        registerAttribute(HeadTextureAttribute.class, HeadTextureAttribute.ID, HeadTextureAttribute.CODEC);
        registerAttribute(ItemCategoryAttribute.class, ItemCategoryAttribute.ID, ItemCategoryAttribute.CODEC);
        registerAttribute(MaterialAttribute.class, MaterialAttribute.ID, MaterialAttribute.CODEC);
        registerAttribute(NameAttribute.class, NameAttribute.ID, NameAttribute.CODEC);
        registerAttribute(StatsAttribute.class, StatsAttribute.ID, StatsAttribute.CODEC);
        lock();
    }
}