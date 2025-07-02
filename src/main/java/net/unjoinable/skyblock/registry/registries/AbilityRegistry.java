package net.unjoinable.skyblock.registry.registries;

import net.kyori.adventure.key.Key;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.item.ability.impls.InstantTransmission;
import net.unjoinable.skyblock.item.ability.impls.WitherImpact;
import net.unjoinable.skyblock.registry.impl.ImmutableRegistry;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A specialized registry for managing SkyBlock item abilities.
 *
 * <p>This registry extends {@link ImmutableRegistry} to provide a type-safe
 * container for {@link ItemAbility} instances, indexed by their
 * {@link java.security.Key} identifiers.</p>
 *
 * <p>The registry is immutable once constructed and supports efficient
 * lookup operations for abilities by their unique identifiers.</p>
 */
public class AbilityRegistry extends ImmutableRegistry<Key, ItemAbility> {

    /**
     * Constructs a new AbilityRegistry from a list of ItemAbility instances.
     *
     * <p>The constructor processes the provided list and creates an internal
     * map where each ability is indexed by its {@link ItemAbility#key()} )}.
     * This enables fast retrieval and ensures the registry remains read-only
     * after construction.</p>
     *
     * @param entries the list of {@link ItemAbility} instances to register.
     *                Must not be null. If duplicate IDs exist, only the last
     *                occurrence is retained in the registry.
     */
    public AbilityRegistry(List<ItemAbility> entries) {
        super(entries
                .stream()
                .collect(Collectors.toMap(
                        ItemAbility::key,
                        Function.identity()
                )));
    }

    /**
     * Creates a new AbilityRegistry pre-populated with built-in default abilities.
     *
     * <p>This factory method is intended for convenient setup of the registry
     * during game startup or testing. It directly registers known hardcoded
     * ability implementations such as {@link InstantTransmission}.</p>
     *
     * @return a new {@link AbilityRegistry} containing default abilities
     */
    public static AbilityRegistry withDefaults() {
        return new AbilityRegistry(List.of(
                new InstantTransmission(),
                new WitherImpact()
        ));
    }
}