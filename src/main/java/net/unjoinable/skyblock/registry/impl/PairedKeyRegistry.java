package net.unjoinable.skyblock.registry.impl;

import net.unjoinable.skyblock.registry.AbstractRegistry;

import java.util.*;

/**
 * A registry implementation that allows retrieval of objects using two keys.
 * Objects are stored with a primary key but can also be retrieved using a secondary key.
 *
 * @param <K1> The type of the primary key
 * @param <K2> The type of the secondary key
 * @param <V>  The type of values stored in this registry
 */
public class PairedKeyRegistry<K1, K2, V> extends AbstractRegistry<K1, V> {

    private final Map<K2, K1> secondaryToPrimary;

    /**
     * Constructs a new PairedKeyRegistry using HashMap for storage.
     */
    public PairedKeyRegistry() {
        super(new HashMap<>());
        this.secondaryToPrimary = new HashMap<>();
    }

    /**
     * Constructs a new PairedKeyRegistry with custom map implementations.
     *
     * @param primaryMap   The map to use for primary key storage
     * @param secondaryMap The map to use for secondary key mapping
     */
    public PairedKeyRegistry(Map<K1, V> primaryMap, Map<K2, K1> secondaryMap) {
        super(primaryMap);
        this.secondaryToPrimary = secondaryMap;
    }

    /**
     * Register a value with both primary and secondary keys.
     *
     * @param primaryKey   The primary key to register the value with
     * @param secondaryKey The secondary key to register the value with
     * @param value        The value to register
     * @return The registry instance for chaining
     */
    public PairedKeyRegistry<K1, K2, V> register(K1 primaryKey, K2 secondaryKey, V value) {
        entries.put(primaryKey, value);
        secondaryToPrimary.put(secondaryKey, primaryKey);
        return this;
    }

    /**
     * Register multiple entries with paired keys.
     *
     * @param pairedEntries A map where each entry contains primary key -> (secondary key, value)
     * @return The registry instance for chaining
     */
    public PairedKeyRegistry<K1, K2, V> registerAllPaired(Map<K1, PairedEntry<K2, V>> pairedEntries) {
        pairedEntries.forEach((primaryKey, pairedEntry) -> {
            entries.put(primaryKey, pairedEntry.value);
            secondaryToPrimary.put(pairedEntry.secondaryKey, primaryKey);
        });
        return this;
    }

    /**
     * Get a value by its secondary key.
     *
     * @param secondaryKey The secondary key to look up
     * @return An Optional containing the value if found, or empty if not found
     */
    public Optional<V> getBySecondaryKey(K2 secondaryKey) {
        K1 primaryKey = secondaryToPrimary.get(secondaryKey);
        if (primaryKey == null) {
            return Optional.empty();
        }
        return get(primaryKey);
    }

    /**
     * Check if the registry contains a value for the specified secondary key.
     *
     * @param secondaryKey The secondary key to check
     * @return true if the registry contains the secondary key, false otherwise
     */
    public boolean containsSecondaryKey(K2 secondaryKey) {
        return secondaryToPrimary.containsKey(secondaryKey);
    }

    /**
     * Get the primary key associated with a secondary key.
     *
     * @param secondaryKey The secondary key
     * @return An Optional containing the primary key if found, or empty if not found
     */
    public Optional<K1> getPrimaryKey(K2 secondaryKey) {
        return Optional.ofNullable(secondaryToPrimary.get(secondaryKey));
    }

    /**
     * Get the secondary key associated with a primary key.
     *
     * @param primaryKey The primary key
     * @return An Optional containing the secondary key if found, or empty if not found
     */
    public Optional<K2> getSecondaryKey(K1 primaryKey) {
        return secondaryToPrimary.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), primaryKey))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    /**
     * Remove a value from the registry by its primary key.
     * This also removes the associated secondary key mapping.
     *
     * @param primaryKey The primary key to remove
     * @return An Optional containing the removed value if found, or empty if not found
     */
    @Override
    public Optional<V> remove(K1 primaryKey) {
        Optional<V> removedValue = super.remove(primaryKey);
        if (removedValue.isPresent()) {
            // Remove the secondary key mapping
            secondaryToPrimary.entrySet().removeIf(entry -> Objects.equals(entry.getValue(), primaryKey));
        }
        return removedValue;
    }

    /**
     * Remove a value from the registry by its secondary key.
     * This also removes the associated primary key entry.
     *
     * @param secondaryKey The secondary key to remove
     * @return An Optional containing the removed value if found, or empty if not found
     */
    public Optional<V> removeBySecondaryKey(K2 secondaryKey) {
        K1 primaryKey = secondaryToPrimary.remove(secondaryKey);
        if (primaryKey == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entries.remove(primaryKey));
    }

    /**
     * Get all secondary keys in the registry.
     *
     * @return A set of all secondary keys
     */
    public Set<K2> secondaryKeys() {
        return Collections.unmodifiableSet(secondaryToPrimary.keySet());
    }

    /**
     * Clear all entries from both primary and secondary key mappings.
     */
    @Override
    public void clear() {
        super.clear();
        secondaryToPrimary.clear();
    }

    /**
         * A helper class to represent a paired entry with secondary key and value.
         *
         * @param <K2> The type of the secondary key
         * @param <V>  The type of the value
         */
        public record PairedEntry<K2, V>(K2 secondaryKey, V value) {

        public static <K2, V> PairedEntry<K2, V> of(K2 secondaryKey, V value) {
                return new PairedEntry<>(secondaryKey, value);
            }
        }
}