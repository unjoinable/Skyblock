package net.unjoinable.skyblock.utils.codec;

import net.kyori.adventure.key.Key;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.registry.registries.AbilityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemAbilityCodec implements Codec<ItemAbility> {
    private final AbilityRegistry registry;

    public ItemAbilityCodec(AbilityRegistry registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull <D> Result<ItemAbility> decode(@NotNull Transcoder<D> coder, @NotNull D value) {
        Result<Key> keyResult = Codec.KEY.decode(coder, value);

        if (keyResult instanceof Result.Ok(Key okKey)) {
            return registry
                    .get(okKey)
                    .<Result<ItemAbility>>map(Result.Ok::new)
                    .orElseGet(() -> new Result.Error<>("Unknown ability ID: " + okKey));
        }

        return new Result.Error<>("Unknown ability: " + value);
    }

    @Override
    public @NotNull <D> Result<D> encode(@NotNull Transcoder<D> coder, @Nullable ItemAbility value) {
        if (value == null) {
            return new Result.Error<>("Can't encode null ItemAbility");
        }

        return Codec.KEY.encode(coder, value.key());
    }
}
