package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

public record NpcSellPriceComponent(int price) implements BasicComponent {
    private static final Tag<Integer> NPC_SELL_PRICE = Tag.Integer("npc_sell_price");

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        builder.setTag(NPC_SELL_PRICE, price);
    }
}
