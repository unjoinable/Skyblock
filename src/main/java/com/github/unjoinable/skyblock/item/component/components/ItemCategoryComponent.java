package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.trait.TransientComponent;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;

/**
 * A class representing the item category of a component.
 */
public final class ItemCategoryComponent implements TransientComponent {
    private final @NotNull ItemCategory itemCategory;

    /**
     * Constructs an ItemCategoryComponent with the specified item category.
     *
     * @param itemCategory The {@link ItemCategory} of the component. Must not be null.
     */
    public ItemCategoryComponent(@NotNull ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    /**
     * Retrieves the item category of this component.
     *
     * @return The {@link ItemCategory} associated with this component.
     */
    public @NotNull ItemCategory getItemCategory() {
        return itemCategory;
    }
}