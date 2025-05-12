package net.skyblock.item.ability.impl;

import net.kyori.adventure.text.Component;
import net.skyblock.item.ability.Ability;
import net.skyblock.item.ability.AbilityType;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Swing implements Ability {

    @Override
    public @NotNull String id() {
        return "swing";
    }

    @Override
    public @NotNull String name() {
        return "Swing";
    }

    @Override
    public @NotNull List<Component> description() {
        List<Component> desc = new ArrayList<>();
        desc.add(text("Throw the bone a short distance", GRAY));
        desc.add(text("dealing the damage an arrow", GRAY));
        desc.add(text("would.", GRAY));
        desc.add(empty());
        desc.add(textOfChildren(text("Deals", GRAY), text(" double damage", RED), text("when", GRAY)));
        desc.add(text("coming back. Pierces up to"));
        desc.add(textOfChildren(text("10", YELLOW), text("foes.", GRAY)));
        return desc;
    }

    @Override
    public @NotNull AbilityType type() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public int cooldown() {
        return 69;
    }

    @Override
    public @NotNull BiConsumer<SkyblockPlayer, SkyblockItem> executeAbility() {
        return null;
    }
}
