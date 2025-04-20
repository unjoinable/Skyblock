package com.github.unjoinable.skyblock.item.service;

import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.components.ItemCategoryComponent;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;

/**
 * Service for resolving item categories from component containers.
 */
public class CategoryResolver {
    /**
     * Resolves the category of an item from its component container.
     * @param container The component container to check
     * @return The item's category, or ItemCategory.NONE if not found
     */
    public @NotNull ItemCategory resolveCategory(@NotNull ComponentContainer container) {
        return container.get(ItemCategoryComponent.class)
                .map(ItemCategoryComponent::getItemCategory)
                .orElse(ItemCategory.NONE);
    }
}