package io.github.unjoinable.item.component.components;

import com.google.common.collect.ImmutableSet;
import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.item.component.Loreable;
import io.github.unjoinable.statistics.Statistic;
import io.github.unjoinable.util.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.unjoinable.statistics.Statistic.*;

public record StatisticsComponent(Map<Statistic, Integer> statistics) implements ItemComponent, Loreable {
    private static final StatisticsComponent DEFAULT = new StatisticsComponent(null);
    private static final Set<Statistic> LORE_ORDER =  ImmutableSet.of(
            DAMAGE, STRENGTH, CRIT_CHANCE, CRIT_DAMAGE, BONUS_ATTACK_SPEED, ABILITY_DAMAGE, SWING_RANGE, HEALTH, DEFENSE,
            SPEED, INTELLIGENCE,  MAGIC_FIND, PET_LUCK, TRUE_DEFENSE, FEROCITY, MINING_SPEED, PRISTINE, MINING_FORTUNE,
            FARMING_FORTUNE, SEA_CREATURE_CHANCE, FISHING_SPEED, HEALTH_REGEN, VITALITY, MENDING);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return StatisticsComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        //another component that does not have apply feature yay
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }

    @Override
    public int priority() {
        return 100; //should be first
    }

    @Override
    public @NotNull List<Component> lore(@NotNull SkyblockItem item) {
        if (statistics == null) return new ArrayList<>();
        List<Component> lore = new ArrayList<>();
        for (Statistic statistic : LORE_ORDER) {
            if (statistics.containsKey(statistic) && statistics.get(statistic) != 0) {
                lore.add(Utils.generateStatisticLore(statistic, statistics.get(statistic)));
            }
        }
        return lore;
    }

}
