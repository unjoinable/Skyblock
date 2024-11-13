package io.github.unjoinable.skyblock.item.component.components;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.LoreableComponent;
import io.github.unjoinable.skyblock.item.component.StatComponent;
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
    public void applyData(@NotNull ComponentContainer container) {
        container.getAllComponents().values().stream()
                .filter(component -> component instanceof StatComponent)
                .map(component -> (StatComponent) component)
                .forEach(component -> {
                     component.statModifiers().forEach((statistic, statModifiers) -> {
                         statistics.getOrDefault(statistic, new StatModifiers()).addModifiers(statModifiers);
                     });
                });
    }

    @Override
    public @NotNull List<Component> lore(SkyblockItem item) {
        List<Component> lore = new ArrayList<>();
        statistics.forEach((statistic, modifiers) -> {
            int value = modifiers.getEffectiveValue();
            if (value != 0) {
                lore.add(Utils.generateStatisticLore(statistic, value));
            }
        });
        return lore;
    }
}
