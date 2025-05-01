package net.skyblock.item.component.service;

import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.impl.ItemCategoryComponent;
import net.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;

/**
 * Service for resolving component from component containers.
 */
public class ComponentResolver {
    /**
     * Resolves the category of an item from its component container.
     * @param container The component container to check
     * @return The item's category, or ItemCategory.NONE if not found
     */
    public @NotNull ItemCategory resolveCategory(@NotNull ComponentContainer container) {
        return container.get(ItemCategoryComponent.class)
                .map(ItemCategoryComponent::itemCategory)
                .orElse(ItemCategory.NONE);
    }
}