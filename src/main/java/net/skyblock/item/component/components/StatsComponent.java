package net.skyblock.item.component.components;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ModifierInfo;
import net.skyblock.item.component.trait.LoreComponent;
import net.skyblock.item.component.trait.StatModifierComponent;
import net.skyblock.item.enums.ModifierType;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.StatValueType;
import net.skyblock.stats.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * Manages item stats, modifiers, and lore generation
 */
public final class StatsComponent implements LoreComponent {
    private final StatProfile baseStats;
    private final ObjectArrayList<StatModifierComponent> modifiers;

    // Stats cache
    private StatProfile finalStatsCache;
    private boolean statsCacheValid = false;

    /**
     * Creates a new StatsComponent with default empty stats
     */
    public StatsComponent() {
        this(new StatProfile(false));
    }

    /**
     * Creates a new StatsComponent with specified base stats
     */
    public StatsComponent(@NotNull StatProfile baseStats) {
        this(baseStats, new ObjectArrayList<>());
    }

    /**
     * Creates a new StatsComponent with specified base stats and modifiers
     */
    public StatsComponent(@NotNull StatProfile baseStats, @NotNull List<StatModifierComponent> modifiers) {
        this.baseStats = Objects.requireNonNull(baseStats);
        this.modifiers = new ObjectArrayList<>(modifiers);
    }

    /**
     * Gets the base stats without modifiers
     */
    public @NotNull StatProfile getBaseStats() {
        return baseStats;
    }

    /**
     * Gets an unmodifiable list of all stat modifiers
     */
    public @NotNull List<StatModifierComponent> getModifiers() {
        return List.copyOf(modifiers);
    }

    /**
     * Adds a base stat with BASE type
     */
    public StatsComponent withBaseStat(@NotNull Statistic stat, double value) {
        return withBaseStat(stat, StatValueType.BASE, value);
    }

    /**
     * Adds a base stat with specified type
     */
    public StatsComponent withBaseStat(@NotNull Statistic stat, @NotNull StatValueType type, double value) {
        var newBase = baseStats.copy();
        newBase.addStat(stat, type, value);
        return new StatsComponent(newBase, modifiers);
    }

    /**
     * Adds a stat modifier
     */
    public StatsComponent withModifier(@NotNull StatModifierComponent modifier) {
        var newModifiers = new ObjectArrayList<>(modifiers);
        newModifiers.add(modifier);
        return new StatsComponent(baseStats, newModifiers);
    }

    /**
     * Removes modifiers of specified type
     */
    public StatsComponent withoutModifier(@NotNull ModifierType type) {
        var newModifiers = new ObjectArrayList<>(modifiers);
        newModifiers.removeIf(modifier -> modifier.getModifierType() == type);
        return new StatsComponent(baseStats, newModifiers);
    }

    /**
     * Calculates the final stats with all modifiers applied
     */
    public @NotNull StatProfile calculateFinalStats(ComponentContainer container) {
        if (statsCacheValid && finalStatsCache != null) {
            return finalStatsCache;
        }

        var result = baseStats.copy();

        for (var modifier : modifiers) {
            result.combineWith(modifier.getStatProfile(container));
        }

        finalStatsCache = result;
        statsCacheValid = true;
        return result;
    }

    /**
     * Calculates stats and returns info about all modifiers for each stat
     */
    public @NotNull Map<Statistic, List<ModifierInfo>> getStatModifierInfo(ComponentContainer container) {
        Map<Statistic, ObjectArrayList<ModifierInfo>> result = new EnumMap<>(Statistic.class);

        findRelevantStats(container, result);
        addModifierInfo(container, result);
        return convertToStandardMap(result);
    }

    /**
     * Finds stats that have base values or are affected by modifiers
     */
    private void findRelevantStats(ComponentContainer container, Map<Statistic, ObjectArrayList<ModifierInfo>> result) {
        for (var stat : Statistic.getValues()) {
            // Add stats with base values
            if (baseStats.get(stat) != 0) {
                result.put(stat, new ObjectArrayList<>());
                continue;
            }

            // Add stats affected by modifiers
            for (var modifier : modifiers) {
                if (modifier.getStatProfile(container).get(stat) != 0) {
                    result.put(stat, new ObjectArrayList<>());
                    break;
                }
            }
        }
    }

    /**
     * Adds modifier info for relevant stats
     */
    private void addModifierInfo(ComponentContainer container, Map<Statistic, ObjectArrayList<ModifierInfo>> result) {
        for (var modifier : modifiers) {
            var modProfile = modifier.getStatProfile(container);

            for (var entry : result.entrySet()) {
                var stat = entry.getKey();
                double value = modProfile.get(stat);
                if (value != 0) {
                    entry.getValue().add(new ModifierInfo(
                            modifier.getModifierType(),
                            value,
                            modifier.getOpenBracket(),
                            modifier.getCloseBracket(),
                            modifier.getModifierColor()
                    ));
                }
            }
        }
    }

    /**
     * Converts FastUtil map to standard map
     */
    private Map<Statistic, List<ModifierInfo>> convertToStandardMap(Map<Statistic, ObjectArrayList<ModifierInfo>> result) {
        var standardMap = new EnumMap<Statistic, List<ModifierInfo>>(Statistic.class);

        for (var entry : result.entrySet()) {
            standardMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        return standardMap;
    }

    @Override
    public int lorePriority() {
        return 0;
    }

    @Override
    public @NotNull List<Component> generateLore(@NotNull ComponentContainer container) {
        var finalStats = calculateFinalStats(container);
        var modifierInfo = getStatModifierInfo(container);
        var lore = new ArrayList<Component>();

        for (var stat : Statistic.getValues()) {
            double value = finalStats.get(stat);
            if (value > 0) {
                lore.add(formatStatLine(
                        stat,
                        value,
                        modifierInfo.getOrDefault(stat, Collections.emptyList())
                ));
            }
        }

        return lore;
    }

    /**
     * Creates a formatted text component for a stat line with modifiers
     */
    private Component formatStatLine(Statistic stat, double value, List<ModifierInfo> modifiers) {
        boolean isPercent = stat.getPercentage();
        String format = isPercent ? "%.1f%%" : "%.0f";
        String formattedValue = String.format(format, value);

        var builder = Component.text()
                .append(Component.text(stat.getDisplayName() + ": ", NamedTextColor.GRAY))
                .append(Component.text("+" + formattedValue + (isPercent ? "%" : ""), stat.getLoreColor()))
                .decoration(ITALIC, false);

        for (var mod : modifiers) {
            String modValue = isPercent
                    ? String.format("%.1f%%", mod.getValue())
                    : String.format("%.0f", mod.getValue());

            builder.append(Component.text(" "))
                    .append(Component.text(
                            mod.getOpenBracket() + "+" + modValue + mod.getCloseBracket(),
                            mod.getColor()
                    ));
        }

        return builder.build();
    }
}