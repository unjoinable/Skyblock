package net.skyblock.item.component.components;

import net.skyblock.item.component.Component;
import net.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;

/**
 * A class representing the item category of a component.
 */
public final class ItemCategoryComponent implements Component {
    private final @NotNull ItemCategory itemCategory;

    /**
     * Constructs an ItemCategoryComponent with the specified item category.
     * @param itemCategory The {@link ItemCategory} of the component. Must not be null.
     */
    public ItemCategoryComponent(@NotNull ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    /**
     * Retrieves the item category of this component.
     * @return The {@link ItemCategory} associated with this component.
     */
    public @NotNull ItemCategory getItemCategory() {
        return itemCategory;
    }
}