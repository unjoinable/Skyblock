package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.Rarity;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record RarityComponent(Rarity rarity) implements LoreableComponent {

    @Override
    public int priority() {
        return 0;  //last component
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        String s1 = rarity.name().replaceAll("_", "");
        String s2 = item.container().getComponent(ItemCategoryComponent.class).category().getName();

        return List.of(Component.text(s1 + " " + s2, rarity.getColor())
                .decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
    }
}
