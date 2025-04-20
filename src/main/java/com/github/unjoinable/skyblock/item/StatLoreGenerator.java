package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.trait.LoreComponent;
import com.github.unjoinable.skyblock.stats.StatProfile;
import com.github.unjoinable.skyblock.stats.Statistic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Component that handles generating stat lore for items.
 */
public final class StatLoreGenerator implements LoreComponent {
    private final StatsCalculator calculator;

    /**
     * Creates a new StatLoreGenerator
     */
    public StatLoreGenerator() {
        this.calculator = new StatsCalculator();
    }

    /**
     * Creates a new StatLoreGenerator with a specific calculator
     * @param calculator The calculator to use for stats
     */
    public StatLoreGenerator(@NotNull StatsCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public int lorePriority() {
        return 75;
    }

    @Override
    public @NotNull List<Component> generateLore(@NotNull ComponentContainer container) {
        List<Component> lore = new ArrayList<>();
        StatProfile stats = calculator.calculateCombinedStats(container);

        // Add all applicable stats to lore
        for (Statistic stat : Statistic.getValues()) {
            float value = stats.get(stat);
            if (value > 0 || stat == Statistic.HEALTH || stat == Statistic.INTELLIGENCE || stat == Statistic.SPEED) {
                lore.add(formatStatLine(stat, value));
            }
        }

        return lore;
    }

    /**
     * Creates a formatted text component for a single stat line
     */
    private Component formatStatLine(Statistic stat, float value) {
        String valueFormat = stat.getPercentage() ? "%.1f%%" : "%.0f";
        String formattedValue = String.format(valueFormat, value);

        TextComponent.Builder builder = Component.text()
                .content(stat.getSymbol() + " ")
                .color(stat.getLoreColor())
                .append(Component.text(stat.getDisplayName() + ": ", stat.getLoreColor()))
                .append(Component.text(formattedValue, stat.getLoreColor()));

        return builder.build();
    }
}
