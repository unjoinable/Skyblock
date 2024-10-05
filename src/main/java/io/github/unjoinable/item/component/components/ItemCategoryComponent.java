package io.github.unjoinable.item.component.components;

import io.github.unjoinable.enums.ItemCategory;
import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemCategoryComponent(ItemCategory category) implements ItemComponent {
    private static final ItemCategoryComponent DEFAULT = new ItemCategoryComponent(ItemCategory.NONE);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return ItemCategoryComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        //does nothing for now.
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }
}
