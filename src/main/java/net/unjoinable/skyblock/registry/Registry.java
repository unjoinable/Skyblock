package net.unjoinable.skyblock.registry;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A generic registry for storing and retrieving objects by key.
 *
 * @param <K> The type of keys used in this registry
 * @param <V> The type of values stored in this registry
 */
public interface Registry<K, V> {

    /**
     * Register a value with the given key.
     *
     * @param key   The key to register the value with
     * @param value The value to register
     * @return The registry instance for chaining
     */
    Registry<K, V> register(K key, V value);

    /**
     * Register multiple entries from a Map.
     *
     * @param entries The entries to register
     * @return The registry instance for chaining
     */
    Registry<K, V> registerAll(Map<K, V> entries);

    /**
     * Get a value by its key.
     *
     * @param key The key to look up
     * @return An Optional containing the value if found, or empty if not found
     */
    Optional<V> get(K key);

    /**
     * Check if the registry contains a value for the specified key.
     *
     * @param key The key to check
     * @return true if the registry contains the key, false otherwise
     */
    boolean contains(K key);

    /**
     * Remove a value from the registry.
     *
     * @param key The key to remove
     * @return An Optional containing the removed value if found, or empty if not found
     */
    Optional<V> remove(K key);

    /**
     * Get all keys in the registry.
     *
     * @return A set of all keys
     */
    Set<K> keys();

    /**
     * Get all values in the registry.
     *
     * @return A set of all values
     */
    Set<V> values();

    /**
     * Clear all entries from the registry.
     */
    void clear();

    /**
     * Get the number of entries in the registry.
     *
     * @return The size of the registry
     */
    int size();
}