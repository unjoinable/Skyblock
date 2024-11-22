package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.statistics.holders.StatModifiersMap;
import io.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record StatisticsComponent(StatModifiersMap statistics) implements LoreableComponent, BasicComponent {

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        List<Component> lore = new ArrayList<>();

        for (Statistic stat : Statistic.getValues()) {
            if (statistics.has(stat)) {
                double value = statistics.get(stat).getEffectiveValue();
                if (value != 0.0D) {
                    lore.add(Utils.generateStatisticLore(stat, value));
                }
            }
        }
        return lore;
    }
}
