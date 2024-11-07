package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import io.github.unjoinable.skyblock.item.component.StatComponent;
import io.github.unjoinable.skyblock.statistics.StatModifier;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public record StatisticsComponent(Map<Statistic, Integer> statistics) implements LoreableComponent {

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        List<Component> lore = new ArrayList<>();
        addAllStatistics(item).forEach((statistic, value) -> {
            if (value != 0) {
                lore.add(Utils.generateStatisticLore(statistic, value));
            }
        });
        return lore;
    }

    public static @NotNull Map<Statistic, Integer> addAllStatistics(SkyblockItem item) {
        ComponentContainer container = item.container();

        Map<Statistic, Integer> statistics = new EnumMap<>(Statistic.class);

        if (container.hasComponent(StatisticsComponent.class)) {
            statistics = container.getComponent(StatisticsComponent.class).statistics();
        }

        Map<Statistic, List<StatModifier>> modifiers = new EnumMap<>(Statistic.class);

        //merging stat modifiers from all components to 1
        container
                .getAllComponents().values().stream()
                .filter(StatComponent.class::isInstance)
                .map(StatComponent.class::cast)
                .forEach(component -> (component).statModifiers()
                        .forEach((statistic, statModifiers) ->
                                modifiers.computeIfAbsent(statistic, _ -> new ArrayList<>())
                                        .addAll(statModifiers)));
        //merged
        for (Statistic statistic : Statistic.getValues()) {
            List<StatModifier> statModifiers = modifiers.get(statistic);
            int additiveValue = 0;
            int multiplicativeValue = 1;
            int baseValue = statistics.getOrDefault(statistic, 0);

            if (statModifiers != null && !statModifiers.isEmpty()) {
                for (StatModifier modifier : statModifiers) {
                    switch (modifier.type()) {
                        case ADDITIVE ->  additiveValue += modifier.value();
                        case MULTIPLICATIVE ->  multiplicativeValue *= modifier.value();
                        case BASE -> baseValue *= modifier.value();
                    }
                }}
            statistics.put(statistic, baseValue*(1 + additiveValue)*multiplicativeValue);
        }
        return statistics;
    }
}
