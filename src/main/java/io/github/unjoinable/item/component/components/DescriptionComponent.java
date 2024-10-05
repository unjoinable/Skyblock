package io.github.unjoinable.item.component.components;

import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.item.component.Loreable;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record DescriptionComponent(List<Component> lore) implements ItemComponent, Loreable {
    private static final DescriptionComponent DEFAULT = new DescriptionComponent(null);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return DescriptionComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        //ignored
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }

    @Override
    public int priority() {
        return 99; //just after statistics I guess.
    }

    @Override
    public @NotNull List<Component> lore(@NotNull SkyblockItem item) {
        if (lore != null) return lore;
        return new ArrayList<>();
    }
}
