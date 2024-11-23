package com.github.unjoinable.skyblock.item.ability.abilities;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.item.ability.AbilityCostType;
import com.github.unjoinable.skyblock.item.ability.AbilityType;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Swing implements Ability {
    private static final NamespacedId ID = new NamespacedId("ability", "swing");

    @Override
    public @NotNull String name() {
        return "Swing";
    }

    @Override
    public @NotNull AbilityCostType costType() {
        return AbilityCostType.NONE;
    }

    @Override
    public @NotNull AbilityType type() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public int abilityCost(@Nullable SkyblockPlayer player) {
        return 0;
    }

    @Override
    public long cooldownInMs() {
        return 0;
    }

    @Override
    public @NotNull List<Component> lore() {
        return List.of();
    }

    @Override
    public void run(@NotNull SkyblockPlayer player, @NotNull SkyblockItem item) {

    }

    @Override
    public @NotNull NamespacedId id() {
        return null;
    }
}
