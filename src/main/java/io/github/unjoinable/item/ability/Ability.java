package io.github.unjoinable.item.ability;

import io.github.unjoinable.enums.AbilityCostType;
import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.user.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Ability {

    @NotNull String name();

    @NotNull String id();

    @NotNull AbilityCostType costType();

    int abilityCost();

    @NotNull List<Component> lore();

    void run(@NotNull SkyblockPlayer player, @NotNull SkyblockItem item);
}

