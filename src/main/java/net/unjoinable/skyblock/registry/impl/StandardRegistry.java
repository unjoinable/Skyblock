package net.unjoinable.skyblock.registry.impl;

import net.unjoinable.skyblock.registry.AbstractRegistry;

import java.util.HashMap;

/**
 * A standard implementation of Registry using HashMap for storage.
 *
 * @param <K> The type of keys used in this registry
 * @param <V> The type of values stored in this registry
 */
public class StandardRegistry<K, V> extends AbstractRegistry<K, V> {

    /**
     * Constructs a new StandardRegistry with a HashMap backend.
     */
    public StandardRegistry() {
        super(new HashMap<>());
    }

    /**
     * Constructs a new StandardRegistry with the specified initial capacity.
     *
     * @param initialCapacity The initial capacity of the underlying HashMap
     */
    public StandardRegistry(int initialCapacity) {
        super(new HashMap<>(initialCapacity));
    }

    /**
     * Constructs a new StandardRegistry with the specified initial capacity and load factor.
     *
     * @param initialCapacity The initial capacity of the underlying HashMap
     * @param loadFactor      The load factor of the underlying HashMap
     */
    public StandardRegistry(int initialCapacity, float loadFactor) {
        super(new HashMap<>(initialCapacity, loadFactor));
    }
}