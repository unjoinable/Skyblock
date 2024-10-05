package io.github.unjoinable.item.component.components;

import io.github.unjoinable.enums.Rarity;
import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record NameComponent(String name) implements ItemComponent {
    private static final NameComponent DEFAULT = new NameComponent(null);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return NameComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        Rarity rarity = item.getOrDefault(RarityComponent.class).rarity();
        builder.customName(Component.text(name).color(rarity.getColor()).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }
}
