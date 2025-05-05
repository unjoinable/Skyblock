package net.skyblock.item.component.handler;

import com.google.gson.JsonElement;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.definition.ItemCategoryComponent;
import net.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class ItemCategoryHandler implements ItemComponentHandler<ItemCategoryComponent> {
    private static final String ID = "category";

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<ItemCategoryComponent> componentType() {
        return ItemCategoryComponent.class;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }

    /**
     * Creates a component instance from JSON data
     *
     * @param json The JSON data to parse
     * @return The created component instance
     * @throws UnsupportedOperationException by default unless overridden
     */
    @Override
    public @NotNull ItemCategoryComponent fromJson(@NotNull JsonElement json) {
        String categoryName = json.getAsString();

        try {
            return new ItemCategoryComponent(ItemCategory.valueOf(categoryName));
        } catch (IllegalArgumentException _) {
            Logger.warn("Invalid item category: {}, defaulting to NONE", categoryName);
            return new ItemCategoryComponent(ItemCategory.NONE);
        }
    }
}
