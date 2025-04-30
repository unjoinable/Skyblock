package net.skyblock.item.components;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.enums.ItemCategory;

/**
 * Component defining the category an item belongs to.
 * <p>
 * Item categories are used for organizing items into logical groups
 * for various gameplay mechanics such as menu filtering and item restrictions.
 */
public record ItemCategoryComponent(ItemCategory itemCategory) implements ItemComponent {

    /**
     * Creates a new ItemCategoryComponent with the specified category.
     *
     * @param itemCategory The category to assign to this item
     * @return A new ItemCategoryComponent with the updated category
     */
    public ItemCategoryComponent with(ItemCategory itemCategory) {
        return new ItemCategoryComponent(itemCategory);
    }
}