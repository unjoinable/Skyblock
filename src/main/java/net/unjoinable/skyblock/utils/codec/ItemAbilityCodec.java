package net.unjoinable.skyblock.utils.codec;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.registry.registries.AbilityRegistry;
import net.unjoinable.skyblock.utils.NamespaceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemAbilityCodec implements Codec<ItemAbility> {
    private final AbilityRegistry registry;

    public ItemAbilityCodec(AbilityRegistry registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull <D> Result<ItemAbility> decode(@NotNull Transcoder<D> coder, @NotNull D value) {
        Result<NamespaceId> idResult = NamespaceId.CODEC.decode(coder, value);

        if (idResult instanceof Result.Ok(NamespaceId okId)) {
            return registry
                    .get(okId)
                    .<Result<ItemAbility>>map(Result.Ok::new)
                    .orElseGet(() -> new Result.Error<>("Unknown ability ID: " + okId));
        }

        return new Result.Error<>("Unknown ability: " + value);
    }

    @Override
    public @NotNull <D> Result<D> encode(@NotNull Transcoder<D> coder, @Nullable ItemAbility value) {
        if (value == null) {
            return new Result.Error<>("Can't encode null ItemAbility");
        }

        return NamespaceId.CODEC.encode(coder, value.id());
    }
}
