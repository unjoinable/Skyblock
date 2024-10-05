package io.github.unjoinable.item.component.components;

import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record NpcSellPriceComponent(int price) implements ItemComponent {
    private static final NpcSellPriceComponent DEFAULT = new NpcSellPriceComponent(-1);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return NpcSellPriceComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        //nothing for now.
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }
}
