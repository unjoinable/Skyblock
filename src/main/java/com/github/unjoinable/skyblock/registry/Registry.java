package com.github.unjoinable.skyblock.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Thread-safe base registry for all Skyblock registries
 * @param <K> Key type (String, NamespacedID, etc)
 * @param <V> Value type (Material, Component, etc)
 */
public abstract class Registry<K, V> implements Registries {
    protected final Map<K, V> registry = new ConcurrentHashMap<>();
    private boolean locked = false;

    /**
     * Registers a new entry
     * @throws IllegalStateException if registry is locked
     * @throws IllegalArgumentException if key already exists
     */
    public void register(@NotNull K key, @NotNull V value) {
        checkLock();
        if (registry.containsKey(key)) {
            throw new IllegalArgumentException("Key already registered: " + key);
        }
        registry.put(key, value);
    }

    /**
     * Gets a registered value
     * @return The value, or null if not found
     */
    public @Nullable V get(@NotNull K key) {
        return registry.get(key);
    }

    /**
     * Finds first value matching predicate
     */
    public @NotNull Optional<V> find(@NotNull Predicate<V> predicate) {
        return registry.values().stream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Finds key for a registered value
     */
    public @NotNull Optional<K> findKey(@NotNull V value) {
        return registry.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), value))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    protected void checkLock() {
        if (locked) throw new IllegalStateException("Registry is locked");
    }

    public void init() {}

    /**
     * Locks the registry from modifications
     */
    public void lock() {
        this.locked = true;
    }
}
