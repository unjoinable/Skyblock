package net.unjoinable.item.attribute.traits;

import net.unjoinable.utility.TranscoderYamlImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents an extension of {@link ItemAttribute} that enables YAML serialization.
 * This interface provides methods for converting attributes to and from YAML elements.
 */
public interface YamlAttribute extends CodecAttribute {

    /**
     * Converts the attribute into an optional YAML-compatible {@link Object} representation
     * using the default YAML {@link TranscoderYamlImpl} instance.
     *
     * @return An {@link Optional} containing the serialized YAML object, or empty if not applicable.
     */
    default @NotNull Optional<Object> asObject() {
        return asObject(TranscoderYamlImpl.INSTANCE);
    }

    default Optional<ItemAttribute> fromObject(@NotNull Object object) {
        return CodecAttribute.super.fromObject(TranscoderYamlImpl.INSTANCE, object);
    }
}