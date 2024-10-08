package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import io.github.unjoinable.skyblock.item.component.StatComponent;
import io.github.unjoinable.skyblock.statistics.StatModifier;
import io.github.unjoinable.skyblock.statistics.StatValueType;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public record StatisticsComponent(Map<Statistic, Integer> statistics) implements BasicComponent, StatComponent, LoreableComponent {

    @Override
    public @NotNull Map<Statistic, List<StatModifier>> statModifiers() {
        Map<Statistic, List<StatModifier>> modifiers = new EnumMap<>(Statistic.class);
        statistics.forEach((statistic, value) -> {
            modifiers.put(statistic, List.of(new StatModifier(StatValueType.BASE, value)));
        });
        return modifiers;
    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public void applyData(@NotNull ComponentContainer container) {
        Map<Statistic, Integer> statistics = new EnumMap<>(Statistic.class);
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
            int baseValue = this.statistics.getOrDefault(statistic, 0);

            if (!statModifiers.isEmpty()) {
                for (StatModifier modifier : statModifiers) {
                    switch (modifier.type()) {
                        case ADDITIVE ->  additiveValue += modifier.value();
                        case MULTIPLICATIVE ->  multiplicativeValue *= modifier.value();
                        case BASE -> baseValue *= modifier.value();
                    }
                }}
            statistics.put(statistic, baseValue*(1 + additiveValue)*multiplicativeValue);
        }
        container.addComponent(new StatisticsComponent(statistics));
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        List<Component> lore = new ArrayList<>();
        statistics.forEach((statistic, value) -> {
            if (value != 0) {
                lore.add(Utils.generateStatisticLore(statistic, value));
            }
        });
        return lore;
    }
}
