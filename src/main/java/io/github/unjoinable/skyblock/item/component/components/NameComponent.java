package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.enums.Rarity;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record NameComponent(String name) implements BasicComponent {

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        Rarity rarity = item.container().getComponent(RarityComponent.class).rarity();
        builder.customName(Component.text(name).color(rarity.getColor()));
    }

}
