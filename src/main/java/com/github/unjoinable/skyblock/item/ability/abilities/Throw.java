package com.github.unjoinable.skyblock.item.ability.abilities;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.item.ability.AbilityCostType;
import com.github.unjoinable.skyblock.item.ability.AbilityType;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.util.MiniString;
import com.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Throw implements Ability {
    private static final NamespacedId ID = new NamespacedId("ability", "throw");
    private static final List<Component> LORE = new ArrayList<>();
    private static final Tag<Long> AXE_THROW_TIME = Tag.Transient("axe_throw_time");
    private static final Tag<Integer> LAST_SPENT_MANA = Tag.Transient("axe_throw_last_consumed");

    static {
        LORE.add(MiniString.toComponent("Throw your axe damaging all enemies"));
    }

    @Override
    public @NotNull String name() {
        return "Throw";
    }

    @Override
    public @NotNull AbilityCostType costType() {
        return AbilityCostType.MANA;
    }

    @Override
    public @NotNull AbilityType type() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public int abilityCost(@Nullable SkyblockPlayer player) {
        return 40;
    }

    @Override
    public long cooldownInMs() {
        return 50;
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
        return ID;
    }
}
