package net.unjoinable.skyblock.registry;

import org.jetbrains.annotations.NotNull;

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
    public @NotNull Registry<K, V> register(@NotNull K key, @NotNull V value) {
        entries.put(key, value);
        return this;
    }

    @Override
    public @NotNull Registry<K, V> registerAll(@NotNull Map<K, V> entries) {
        this.entries.putAll(entries);
        return this;
    }

    @Override
    public @NotNull Optional<V> get(@NotNull K key) {
        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public boolean contains(@NotNull K key) {
        return entries.containsKey(key);
    }

    @Override
    public @NotNull Optional<V> remove(@NotNull K key) {
        return Optional.ofNullable(entries.remove(key));
    }

    @Override
    public @NotNull Set<K> keys() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @Override
    public @NotNull Set<V> values() {
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
    public <T> @NotNull Optional<T> findFirstByClass(@NotNull Class<T> clazz) {
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
    public <T> @NotNull List<T> findAllByClass(@NotNull Class<T> clazz) {
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
    public int countByClass(@NotNull Class<?> clazz) {
        return (int) entries.values().stream()
                .filter(clazz::isInstance)
                .count();
    }
}