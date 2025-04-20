package com.github.unjoinable.skyblock.registry;

import com.github.unjoinable.skyblock.item.component.Component;

/**
 * Registry for mapping component IDs to their corresponding Component classes.
 *
 * Example:
 * register("rarity", RarityComponent.class);
 */
public class ComponentRegistry extends Registry<String, Class<? extends Component>> {
    // Inherits all logic from Registry
}
