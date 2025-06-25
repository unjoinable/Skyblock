package net.unjoinable.skyblock.registry;

import java.util.*;

/**
 * Abstract implementation of Registry that provides common functionality.
 *
 * @param <K> The type of keys used in this registry
 * @param <V> The type of values stored in this registry
 */
public abstract class AbstractRegistry<K, V> implements Registry<K, V> {

    protected final Map<K, V> entries;

    /**
     * Construct a new AbstractRegistry with the provided map implementation.
     *
     * @param entries The map to use for storing registry entries
     */
    protected AbstractRegistry(Map<K, V> entries) {
        this.entries = entries;
    }

    @Override
    public Registry<K, V> register(K key, V value) {
        entries.put(key, value);
        return this;
    }

    @Override
    public Registry<K, V> registerAll(Map<K, V> entries) {
        this.entries.putAll(entries);
        return this;
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public boolean contains(K key) {
        return entries.containsKey(key);
    }

    @Override
    public Optional<V> remove(K key) {
        return Optional.ofNullable(entries.remove(key));
    }

    @Override
    public Set<K> keys() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @Override
    public Set<V> values() {
        return Set.copyOf(entries.values());
    }

    @Override
    public void clear() {
        entries.clear();
    }

    @Override
    public int size() {
        return entries.size();
    }

    // Class look up methods

    /**
     * Find the first value that is an instance of the specified class.
     *
     * @param clazz The class to look for
     * @param <T>   The type of the class
     * @return An Optional containing the first matching value, or empty if none found
     */
    public <T> Optional<T> findFirstByClass(Class<T> clazz) {
        return entries.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst();
    }

    /**
     * Find all values that are instances of the specified class.
     *
     * @param clazz The class to look for
     * @param <T>   The type of the class
     * @return A List containing all matching values
     */
    public <T> List<T> findAllByClass(Class<T> clazz) {
        return entries.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    /**
     * Count the number of values that are instances of the specified class.
     *
     * @param clazz The class to count
     * @return The count of matching values
     */
    public int countByClass(Class<?> clazz) {
        return (int) entries.values().stream()
                .filter(clazz::isInstance)
                .count();
    }
}