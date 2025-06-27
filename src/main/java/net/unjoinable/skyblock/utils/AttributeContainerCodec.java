package net.unjoinable.skyblock.utils;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttributeContainerCodec implements Codec<AttributeContainer> {
    private final CodecRegistry registry;

    /**
     * Creates a new AttributeContainerCodec with the specified registry.
     *
     * @param registry the codec registry containing attribute codecs
     */
    public AttributeContainerCodec(CodecRegistry registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull <D> Result<AttributeContainer> decode(@NotNull Transcoder<D> coder, @NotNull D value) {
        Result<List<D>> listResult = coder.getList(value);
        if (!(listResult instanceof Result.Ok<List<D>>(var attributeList))) {
            return new Result.Error<>("Not a valid attribute container - expected list");
        }

        AttributeContainer.Builder builder = AttributeContainer.builder();
        Set<Codec<? extends ItemAttribute>> availableCodecs = new HashSet<>(registry.values());

        for (D attributeData : attributeList) {
            ItemAttribute decodedAttribute = tryDecodeAttribute(coder, attributeData, availableCodecs);
            if (decodedAttribute != null) {
                builder.with(decodedAttribute);
            }
        }

        return new Result.Ok<>(builder.build());
    }

    @Override
    public @NotNull <D> Result<D> encode(@NotNull Transcoder<D> coder, @Nullable AttributeContainer value) {
        if (value == null) {
            return new Result.Error<>("Cannot encode a null AttributeContainer");
        }

        Transcoder.ListBuilder<D> listBuilder = coder.createList(value.size());
        for (ItemAttribute attribute : value) {
            D encoded = encodeAttribute(coder, attribute);
            if (encoded != null) {
                listBuilder.add(encoded);
            }
        }

        return new Result.Ok<>(listBuilder.build());
    }

    /**
     * Attempts to decode a single attribute using the first matching codec from the available set.
     * The matching codec is removed from the available set to prevent duplicate usage.
     *
     * @param coder the transcoder to use for decoding
     * @param attributeData the raw attribute data to decode
     * @param availableCodecs the set of available codecs, modified when a match is found
     * @param <D> the data type being transcoded
     * @return the decoded ItemAttribute, or null if no codec could decode the data
     */
    private <D> @Nullable ItemAttribute tryDecodeAttribute(
            Transcoder<D> coder,
            D attributeData,
            Set<Codec<? extends ItemAttribute>> availableCodecs) {

        for (Codec<? extends ItemAttribute> codec : availableCodecs) {
            if (codec.decode(coder, attributeData) instanceof Result.Ok<? extends ItemAttribute>(var attribute)) {
                availableCodecs.remove(codec);
                return attribute;
            }
        }
        return null;
    }

    /**
     * Encodes a single ItemAttribute using its associated codec.
     *
     * @param transcoder the transcoder to use for encoding
     * @param attribute the ItemAttribute to encode
     * @param <D> the data type being transcoded
     * @return the encoded data, or null if encoding failed
     */
    private <D> @Nullable D encodeAttribute(Transcoder<D> transcoder, ItemAttribute attribute) {
        return attribute.safeCodec().encode(transcoder, attribute) instanceof Result.Ok<D>(var encoded)
                ? encoded
                : null;
    }
}