package com.github.unjoinable.skyblock.registry;

/**
 * Core registry interface defining all singleton registry instances.
 * <p>
 * All registries should be accessed through these static instances.
 */
interface Registries {
    /**
     * Global instance of the NBT-readable component registry.
     * @apiNote Automatically initializes on first access
     */
    ReadableComponentRegistry READABLE_COMPONENT_REGISTRY = new ReadableComponentRegistry();

    /**
     * Global instance of the SkyblockItem registry.
     * @apiNote Automatically initializes on first access
     */
    ItemRegistry ITEM_REGISTRY = new ItemRegistry();
}