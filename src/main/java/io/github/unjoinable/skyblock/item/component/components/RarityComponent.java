package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.enums.Rarity;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.unjoinable.skyblock.util.MiniMessageTemplate.MM;

public record RarityComponent(Rarity rarity) implements LoreableComponent {

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        String s1 = STR."\{rarity.name().replaceAll("_", "")}";
        String s2 = item.container().getComponent(ItemCategoryComponent.class).category().getName();

        return List.of(MM."<b>\{s1} \{s2}".color(rarity.getColor()));
    }
}
