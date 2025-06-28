package net.unjoinable.skyblock.utils.codec;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.registry.registries.CodecRegistry;
import net.unjoinable.skyblock.utils.NamespaceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AttributeContainerCodec implements Codec<AttributeContainer> {
    private final CodecRegistry registry;

    public AttributeContainerCodec(CodecRegistry registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull <D> Result<AttributeContainer> decode(@NotNull Transcoder<D> coder, @NotNull D value) {
        if (!(coder.getMap(value) instanceof Result.Ok<Transcoder.MapLike<D>>(var mapLike))) {
            return new Result.Error<>("Not a valid attribute container - expected map");
        }

        var builder = AttributeContainer.builder();
        for (NamespaceId id : registry.secondaryKeys()) {
            String idStr = id.toString();

            if (mapLike.hasValue(idStr) && mapLike.getValue(idStr) instanceof Result.Ok<D>(var data)) {
                registry.getCodecByNamespace(id).ifPresent(codec -> {
                    if (codec.decode(coder, data) instanceof Result.Ok(ItemAttribute attr)) {
                        builder.with(attr);
                    }
                });
            }
        }
        return new Result.Ok<>(builder.build());
    }

    @Override
    public @NotNull <D> Result<D> encode(@NotNull Transcoder<D> coder, @Nullable AttributeContainer value) {
        if (value == null) {
            return new Result.Error<>("Cannot encode a null AttributeContainer");
        }

        var mapBuilder = coder.createMap();
        for (ItemAttribute attr : value) {
            if (attr.safeCodec().encode(coder, attr) instanceof Result.Ok<D>(var encoded)) {
                mapBuilder.put(attr.id().toString(), encoded);
            }
        }

        return new Result.Ok<>(mapBuilder.build());
    }
}