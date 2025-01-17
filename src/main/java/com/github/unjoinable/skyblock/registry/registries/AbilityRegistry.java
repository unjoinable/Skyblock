package com.github.unjoinable.skyblock.registry.registries;

import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.item.ability.abilities.InstantTransmission;
import com.github.unjoinable.skyblock.item.ability.abilities.Throw;
import com.github.unjoinable.skyblock.registry.Registry;
import com.github.unjoinable.skyblock.util.NamespacedId;
import org.jetbrains.annotations.NotNull;

public class AbilityRegistry extends Registry<NamespacedId, Ability> {
    private final static AbilityRegistry INSTANCE = new AbilityRegistry();

    public void registerAbility(@NotNull Ability value) {
        super.add(value.id(), value);
    }

    public @NotNull Ability getRegisteredAbility(@NotNull String stringId) {
        NamespacedId id = NamespacedId.fromString(stringId);
        return get(id);
    }

    @Override
    public void registerAll() {
        registerAbility(new InstantTransmission());
        registerAbility(new Throw());
    }

    public static AbilityRegistry getInstance() {
        return INSTANCE;
    }
}
