package io.github.unjoinable.item.component.components;

import io.github.unjoinable.enums.Rarity;
import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.item.component.Loreable;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.unjoinable.util.MiniMessageTemplate.MM;

public record RarityComponent(Rarity rarity) implements ItemComponent, Loreable {
    private static final RarityComponent DEFAULT = new RarityComponent(Rarity.UNOBTAINABLE);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return RarityComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        //does not do anything for now.
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }

    @Override
    public int priority() {
        return 0; //should be last.
    }

    @Override
    public @NotNull List<Component> lore(@NotNull SkyblockItem item) {
        String s1 = STR."\{rarity.name().replaceAll("_", "")}";
        String s2 = item.getOrDefault(ItemCategoryComponent.class).category().getName();

        return List.of(MM."<b>\{s1} \{s2}".color(rarity.getColor()));
    }
}
