package io.github.unjoinable.skyblock.item.ability;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
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

