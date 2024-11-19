package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import io.github.unjoinable.skyblock.statistics.StatModifiers;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record StatisticsComponent(Map<Statistic, StatModifiers> statistics) implements LoreableComponent, BasicComponent {

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        List<Component> lore = new ArrayList<>();
        for (Statistic stat : Statistic.getValues()) {
            if (statistics.containsKey(stat)) {
                double value = statistics.get(stat).getEffectiveValue();
                if (value!= 0D) {
                    lore.add(Utils.generateStatisticLore(stat, value));
                }
            }
        }
        return lore;
    }
}
