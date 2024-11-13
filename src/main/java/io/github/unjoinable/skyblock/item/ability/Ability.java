package io.github.unjoinable.skyblock.item.ability;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedObject;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Ability extends NamespacedObject {

    @NotNull String name();

    @NotNull AbilityCostType costType();

    @NotNull AbilityType type();

    int abilityCost();

    long cooldownInMs();

    @NotNull List<Component> lore();

    void run(@NotNull SkyblockPlayer player, @NotNull SkyblockItem item);
}

