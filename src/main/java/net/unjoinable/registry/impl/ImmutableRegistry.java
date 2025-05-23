package net.unjoinable.registry.impl;

import net.unjoinable.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An immutable registry implementation that gets populated during construction
 * and cannot be modified afterward. Any attempts to modify the registry will
 * result in an UnsupportedOperationException.
 *
 * @param <K> The type of keys used in this registry
 * @param <V> The type of values stored in this registry
 */
public class ImmutableRegistry<K, V> implements Registry<K, V> {

    private final Map<K, V> entries;

    /**
     * Constructs a new ImmutableRegistry with the provided entries.
     *
     * @param entries The entries to populate the registry with
     */
    public ImmutableRegistry(@NotNull Map<K, V> entries) {
        // Create a defensive copy to ensure true immutability
        this.entries = Map.copyOf(entries);
    }

    /**
     * Creates a new ImmutableRegistry from an existing registry.
     *
     * @param registry The registry to copy entries from
     */
    public ImmutableRegistry(@NotNull Registry<K, V> registry) {
        Map<K, V> tempMap = new HashMap<>();
        registry.keys().forEach(key -> registry.get(key).ifPresent(value -> tempMap.put(key, value)));
        this.entries = Collections.unmodifiableMap(tempMap);
    }

    @Override
    public @NotNull Registry<K, V> register(@NotNull K key, @NotNull V value) {
        throw new UnsupportedOperationException("Cannot modify an ImmutableRegistry");
    }

    @Override
    public @NotNull Registry<K, V> registerAll(@NotNull Map<K, V> entries) {
        throw new UnsupportedOperationException("Cannot modify an ImmutableRegistry");
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
        throw new UnsupportedOperationException("Cannot modify an ImmutableRegistry");
    }

    @Override
    public @NotNull Set<K> keys() {
        return entries.keySet(); // Already unmodifiable because of unmodifiableMap
    }

    @Override
    public @NotNull Set<V> values() {
        return Set.copyOf(entries.values());
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Cannot modify an ImmutableRegistry");
    }

    @Override
    public int size() {
        return entries.size();
    }
}