package net.skyblock.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * A thread-safe, bidirectional, generic registry.
 * Supports key-to-value and value-to-key lookups.
 *
 * @param <K> The key type (e.g., String, NamespacedID)
 * @param <V> The value type (e.g., Component, SkyblockItem)
 */
public abstract class Registry<K, V> implements Iterable<V> {
    protected final Map<K, V> keyToValue = new ConcurrentHashMap<>();
    protected final Map<V, K> valueToKey = new ConcurrentHashMap<>();
    private boolean locked = false;

    /**
     * Registers a new entry.
     *
     * @param key   The key to register
     * @param value The value to associate
     * @throws IllegalStateException    if registry is locked
     * @throws IllegalArgumentException if the key is already registered
     */
    public void register(@NotNull K key, @NotNull V value) {
        checkLock();
        if (keyToValue.containsKey(key)) {
            throw new IllegalArgumentException("Key already registered: " + key);
        }
        keyToValue.put(key, value);
        valueToKey.put(value, key);
    }

    /**
     * Retrieves a value by its key.
     *
     * @param key The key to search
     * @return The value, or null if not found
     */
    public @Nullable V get(@NotNull K key) {
        return keyToValue.get(key);
    }

    /**
     * Retrieves a key by its value.
     *
     * @param value The value to search
     * @return The key, or null if not found
     */
    public @Nullable K getKey(@NotNull V value) {
        return valueToKey.get(value);
    }

    /**
     * Finds the first value matching the given predicate.
     *
     * @param predicate Predicate to apply
     * @return Optional containing the value, or empty if none match
     */
    public @NotNull Optional<V> find(@NotNull Predicate<V> predicate) {
        return keyToValue.values().stream().filter(predicate).findFirst();
    }

    /**
     * Finds the first key matching the given predicate on values.
     *
     * @param predicate Predicate to apply
     * @return Optional containing the key, or empty if none match
     */
    public @NotNull Optional<K> findKey(@NotNull Predicate<V> predicate) {
        return keyToValue.entrySet().stream()
                .filter(e -> predicate.test(e.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    /**
     * Initializes the registry.
     * Override in subclasses to populate entries.
     */
    public void init() {}

    /**
     * Locks the registry to prevent further registration.
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * Checks if the registry is locked and throws if it is.
     */
    protected void checkLock() {
        if (locked) {
            throw new IllegalStateException("Registry is locked");
        }
    }

    /**
     * Checks if a key exists in the registry.
     */
    public boolean containsKey(@NotNull K key) {
        return keyToValue.containsKey(key);
    }

    /**
     * Checks if a value exists in the registry.
     */
    public boolean containsValue(@NotNull V value) {
        return valueToKey.containsKey(value);
    }

    /**
     * Returns an unmodifiable view of all registered keys.
     */
    public @NotNull Set<K> keys() {
        return Collections.unmodifiableSet(keyToValue.keySet());
    }

    /**
     * Returns an unmodifiable view of all registered values.
     */
    public @NotNull Collection<V> values() {
        return Collections.unmodifiableCollection(keyToValue.values());
    }

    @Override
    public @NotNull Iterator<V> iterator() {
        return values().iterator();
    }
}
