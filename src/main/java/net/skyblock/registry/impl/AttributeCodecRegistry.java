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
     * Registers a new codec with its associated attribute class and tag key.
     *
     * @param attributeClass The attribute class to use as a key
     * @param tagKey         The string key to use for the NBT tag
     * @param codec          The codec to associate with the attribute class
     * @throws IllegalStateException    if registry is locked
     * @throws IllegalArgumentException if the key is already registered
     */
    public void registerAttribute(@NotNull Class<? extends ItemAttribute> attributeClass, @NotNull String tagKey, @NotNull Codec<?> codec) {
        Tag<BinaryTag> tag = Tag.NBT(tagKey);
        cachedTags.put(tagKey, tag);
        stringToClass.put(tagKey, attributeClass);
        classToString.put(attributeClass, tagKey);
        super.register(attributeClass, codec);
    }

    /**
     * Alternative register method that generates the tag key from the class name.
     *
     * @param attributeClass The attribute class to use as a key
     * @param codec          The codec to associate with the attribute class
     * @throws IllegalStateException    if registry is locked
     * @throws IllegalArgumentException if the key is already registered
     */
    @Override
    public void register(@NotNull Class<? extends ItemAttribute> attributeClass, @NotNull Codec<?> codec) {
        String tagKey = generateTagKey(attributeClass);
        registerAttribute(attributeClass, tagKey, codec);
    }

    /**
     * Registers a codec with an attribute class and an optional custom tag key.
     *
     * @param attributeClass The attribute class to register
     * @param codec The codec to register
     * @param customTagKey Optional custom tag key (if null, will be generated from class name)
     * @param <A> The attribute type
     */
    public <A extends ItemAttribute> void registerCodec(@NotNull Class<A> attributeClass,
                                                        @NotNull Codec<?> codec,
                                                        String customTagKey) {
        String tagKey = customTagKey != null ? customTagKey : generateTagKey(attributeClass);
        registerAttribute(attributeClass, tagKey, codec);
    }

    /**
     * Registers a codec with an attribute class using an auto-generated tag key.
     *
     * @param attributeClass The attribute class to register
     * @param codec The codec to register
     * @param <A> The attribute type
     */
    public <A extends ItemAttribute> void registerCodec(@NotNull Class<A> attributeClass,
                                                        @NotNull Codec<?> codec) {
        registerCodec(attributeClass, codec, null);
    }

    /**
     * Generates a tag key from a class by using its simple name and converting to lowercase.
     *
     * @param clazz The class to generate a tag key for
     * @return The generated tag key
     */
    private String generateTagKey(@NotNull Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase().replace("attribute", "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<Class<? extends ItemAttribute>> getAttributeClass(@NotNull String tagKey) {
        return Optional.ofNullable(stringToClass.get(tagKey));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<String> getTagKey(@NotNull Class<? extends ItemAttribute> attributeClass) {
        return Optional.ofNullable(classToString.get(attributeClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<Tag<BinaryTag>> getTag(@NotNull String key) {
        return Optional.ofNullable(cachedTags.get(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<Tag<BinaryTag>> getTagForClass(@NotNull Class<? extends ItemAttribute> attributeClass) {
        return getTagKey(attributeClass).flatMap(this::getTag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull Optional<Codec<T>> getCodec(@NotNull String key) {
        return getAttributeClass(key).map(this::get).map(codec -> (Codec<T>) codec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, A extends ItemAttribute> @NotNull Optional<Codec<T>> getCodecForClass(@NotNull Class<A> attributeClass) {
        return Optional.ofNullable((Codec<T>) get(attributeClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Map<String, Tag<BinaryTag>> getTags() {
        return Collections.unmodifiableMap(cachedTags);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull Set<Class<? extends ItemAttribute>> getAttributeClasses() {
        return Set.copyOf(keyToValue.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCodec(@NotNull String key) {
        return stringToClass.containsKey(key) && containsKey(stringToClass.get(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCodecForClass(@NotNull Class<? extends ItemAttribute> attributeClass) {
        return containsKey(attributeClass);
    }

    /**
     * Initializes the registry with default attribute codecs.
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