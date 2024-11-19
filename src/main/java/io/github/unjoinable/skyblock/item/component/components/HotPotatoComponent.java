package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.*;
import io.github.unjoinable.skyblock.statistics.StatModifier;
import io.github.unjoinable.skyblock.statistics.Statistic;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record HotPotatoComponent(int potatoes) implements BasicComponent, ModifierComponent, StatComponent {
    private static final Tag<Integer> POTATO_COUNT = Tag.Integer("potato_count");

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        builder.setTag(POTATO_COUNT, potatoes);
    }

    @Override
    public @Nullable Component retrieve(ItemStack.@NotNull Builder builder) {
        ItemStack item = builder.build();
        if (item.hasTag(POTATO_COUNT)) {
            return new HotPotatoComponent(item.getTag(POTATO_COUNT));
        }
        return null;
    }

    @Override
    public @NotNull Map<Statistic, List<StatModifier>> statModifiers(@NotNull ComponentContainer container) {

        return Map.of();
    }
}
