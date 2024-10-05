package io.github.unjoinable.item.component.components;

import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record DungeonComponent(boolean isDungeonItem) implements ItemComponent {
    private static final DungeonComponent DEFAULT = new DungeonComponent(false);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return DungeonComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        //does not do anything yet
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }
}
