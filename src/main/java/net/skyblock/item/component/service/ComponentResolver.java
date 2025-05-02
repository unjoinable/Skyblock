package net.skyblock.item.component.service;

import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.impl.ItemCategoryComponent;
import net.skyblock.item.component.impl.RarityComponent;
import net.skyblock.item.enums.ItemCategory;
import net.skyblock.item.enums.Rarity;
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

    /**
     * Resolves the rarity of an item from its component container.
     * It gives the upgraded rarity if it is upgraded.
     * @param container The component container to check
     * @return The item's rarity, UNOBTAINABLE if not found
     */
    public @NotNull Rarity resolveRarity(@NotNull ComponentContainer container) {
        return container.get(RarityComponent.class)
                .map(component -> component.isUpgraded()
                        ? component.rarity().upgrade()
                        : component.rarity())
                .orElse(Rarity.UNOBTAINABLE);
    }
}