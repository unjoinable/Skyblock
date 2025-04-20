package com.github.unjoinable.skyblock.registry;

/**
 * Core registry interface defining all singleton registry instances.
 * <p>
 * All registries should be accessed through these static instances.
 */
interface Registries {

    /**
     * Global instance of the SkyblockItem registry.
     */
    ItemRegistry ITEM_REGISTRY = new ItemRegistry();

    /**
     * Global instance of the Component registry.
     */
    ComponentRegistry COMPONENT_REGISTRY = new ComponentRegistry();
}