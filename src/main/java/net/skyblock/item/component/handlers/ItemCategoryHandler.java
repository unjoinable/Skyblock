package net.skyblock.item.component.handlers;

import com.google.gson.JsonObject;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.impl.ItemCategoryComponent;
import net.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;

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
    public ItemCategoryComponent fromJson(@NotNull JsonObject json) {
        if (json.has(ID)) {
            String value = json.get(ID).getAsString();
            ItemCategory category = ItemCategory.valueOf(value);
            return new ItemCategoryComponent(category);
        }
        return new ItemCategoryComponent(ItemCategory.NONE);
    }
}
