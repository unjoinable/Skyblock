package com.github.unjoinable.skyblock.registry;

import com.github.unjoinable.skyblock.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param <K> The registry key type.
 * @param <V> The registry value type.
 * @since 1.0.0
 * @author unjoinable
 */
public abstract class Registry<K, V> implements Iterable<V> {
    private final Map<K, V> objects;

    /**
     * Creates the instance of registry
     * @since 1.0.0
     */
    public Registry() {
        this.objects = new ConcurrentHashMap<>();
    }

    /**
     * Gets the object from registry and gives it, if the key was invalid it will throw an exception.
     * @param key The key of the value wanted from the registry.
     * @return The value from the key provided.
     * @throws IllegalArgumentException In case if an invalid key is provided.
     * @since 1.0.0
     */
    public V get(K key) {
        if (objects.containsKey(key)) {
            return objects.get(key);
        } else {
            throw new IllegalArgumentException(StringUtils.formatString("Object {} does not exist in the registry", key));
        }
    }

    /**
     * @param key The key to look for.
     * @param defaultValue Default value.
     * @return The value.
     * @since 1.0.0
     */
    public V getOrDefault(K key, V defaultValue) {
        return objects.getOrDefault(key, defaultValue);
    }

    /**
     * It adds the provided key, value to the registry if the key already exists it throws an exception.
     * @param key The key of the value to be inserted into the registry.
     * @param value The value of the key to be inserted.
     * @throws IllegalArgumentException In case if the key already exists in the registry.
     * @since 1.0.0
     */
    public void add(K key, V value) {
        if (objects.containsKey(key)) {
            throw new IllegalArgumentException(StringUtils.formatString("Object already exists in the registry {} {}", key, value));
        } else {
            objects.put(key, value);
        }
    }

    /**
     * @param key The key which is to be removed from the registry.
     * @throws IllegalArgumentException When the object is not present in the registry this exception is thrown.
     * @since 1.0.0
     */
    public void remove(K key) {
        if (objects.containsKey(key)) {
            objects.remove(key);
        } else {
            throw new IllegalArgumentException("Object does not exist in the registry");
        }
    }

    /**
     * @param key The object to check if it exists in the registry.
     * @return If the object is present or not.
     * @since 1.0.0
     */
    public boolean contains(K key) {
        return objects.containsKey(key);
    }

    /**
     * @return Returns the map of all present objects in the registry
     * @since 1.0.0
     */
    public Map<K, V> getAllObjects() {
        return objects;
    }

    public void registerAll() {} //method ran to register the registries.

    @Override
    public @NotNull Iterator<V> iterator() {
        return objects.values().iterator();
    }
}