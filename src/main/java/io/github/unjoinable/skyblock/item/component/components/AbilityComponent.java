package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public record AbilityComponent(@NotNull Ability ability) implements BasicComponent, LoreableComponent {
    public static final Tag<String> ABILITY_TAG = Tag.String("ability");

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        builder.set(ABILITY_TAG, ability.id().toString());
    }

    @Override
    public int priority() {
        return 2; //before rarity and item requirement
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Ability: " + ability.name() + " ", GOLD).decoration(ITALIC, false).append(Component.text(ability.type().toString(), YELLOW, BOLD)));
        lore.addAll(ability.lore());
        lore.add(Component.text(ability.costType() + " Cost: ", DARK_GRAY).decoration(ITALIC, false).append(Component.text(ability().abilityCost(), DARK_AQUA)));
        return lore;
    }
}
