package net.unjoinable.skyblock.registry.impl;

import net.unjoinable.skyblock.registry.Registry;
import org.intellij.lang.annotations.Subst;

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
    public ImmutableRegistry(Map<K, V> entries) {
        // Create a defensive copy to ensure true immutability
        this.entries = Map.copyOf(entries);
    }

    /**
     * Creates a new ImmutableRegistry from an existing registry.
     *
     * @param registry The registry to copy entries from
     */
    public ImmutableRegistry(Registry<K, V> registry) {
        Map<K, V> tempMap = new HashMap<>();
        registry.keys().forEach(key -> registry.get(key).ifPresent(value -> tempMap.put(key, value)));
        this.entries = Collections.unmodifiableMap(tempMap);
    }

    @Override
    public Registry<K, V> register(K key, V value) {
        throw new UnsupportedOperationException("Cannot modify an ImmutableRegistry");
    }

    @Override
    public Registry<K, V> registerAll(Map<K, V> entries) {
        throw new UnsupportedOperationException("Cannot modify an ImmutableRegistry");
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
        throw new UnsupportedOperationException("Cannot modify an ImmutableRegistry");
    }

    @Override
    public Set<K> keys() {
        return entries.keySet(); // Already unmodifiable because of unmodifiableMap
    }

    @Subst("")
    @Override
    public Set<V> values() {
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