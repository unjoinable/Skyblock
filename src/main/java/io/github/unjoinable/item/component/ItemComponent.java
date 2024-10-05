package io.github.unjoinable.item.component;

import io.github.unjoinable.item.SkyblockItem;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemComponent {

    @NotNull Class<? extends ItemComponent> type();

    void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder);

    @NotNull ItemComponent defaultComponent();
}
