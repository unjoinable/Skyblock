package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.Rarity;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record NameComponent(String name) implements BasicComponent {

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        Rarity rarity = item.container().getComponent(RarityComponent.class).rarity();
        Component colouredName = Component.text(name).decoration(TextDecoration.ITALIC, false);

        if (rarity == Rarity.UNOBTAINABLE) builder.customName(colouredName);
        builder.customName(colouredName.color(rarity.getColor()));
    }

}
