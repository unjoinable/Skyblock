package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record DescriptionComponent(List<Component> description) implements LoreableComponent {

    @Override
    public int priority() {
        return 50;
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        return description;
    }
}
