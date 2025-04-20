package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.trait.NonPersistentComponent;
import com.github.unjoinable.skyblock.item.component.trait.StatModifierComponent;
import com.github.unjoinable.skyblock.item.enums.ModifierType;
import com.github.unjoinable.skyblock.stats.StatProfile;
import com.github.unjoinable.skyblock.stats.Statistic;
import com.github.unjoinable.skyblock.stats.StatValueType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Unified component for handling item stats and lore generation.
 * This component manages both base stats and all stat modifiers in one place.
 */
public final class ItemStatsComponent implements NonPersistentComponent {
    private final StatProfile baseStats;
    private final List<StatModifierComponent> modifiers;
    private StatProfile cachedCombinedStats;

    /**
     * Creates a new ItemStatsComponent with default base stats
     */
    public ItemStatsComponent() {
        this.baseStats = new StatProfile(false);
        this.modifiers = new ArrayList<>();
        this.cachedCombinedStats = null;
    }

    /**
     * Creates a new ItemStatsComponent with specified base stats
     *
     * @param baseStats The base stats for this item
     */
    public ItemStatsComponent(@NotNull StatProfile baseStats) {
        this.baseStats = Objects.requireNonNull(baseStats);
        this.modifiers = new ArrayList<>();
        this.cachedCombinedStats = null;
    }

    /**
     * Private constructor for creating modified copies
     */
    private ItemStatsComponent(StatProfile baseStats, List<StatModifierComponent> modifiers) {
        this.baseStats = baseStats;
        this.modifiers = modifiers;
        this.cachedCombinedStats = null;
    }

    /**
     * Gets the base stats of the item without modifiers
     *
     * @return The base stat profile
     */
    public StatProfile getBaseStats() {
        return baseStats;
    }

    /**
     * Gets an unmodifiable list of all stat modifiers
     *
     * @return List of stat modifiers
     */
    public List<StatModifierComponent> getModifiers() {
        return Collections.unmodifiableList(modifiers);
    }

    /**
     * Gets the combined stats with all modifiers applied
     *
     * @return A stat profile with all stats combined
     */
    public StatProfile getCombinedStats() {
        if (cachedCombinedStats == null) {
            cachedCombinedStats = calculateCombinedStats();
        }
        return cachedCombinedStats;
    }

    /**
     * Returns a new component with the given base stat added or modified
     *
     * @param stat The statistic to modify
     * @param value The value to set/add
     * @return A new component with the modification
     */
    public ItemStatsComponent withBaseStat(Statistic stat, float value) {
        return withBaseStat(stat, StatValueType.BASE, value);
    }

    /**
     * Returns a new component with the given base stat added or modified
     *
     * @param stat The statistic to modify
     * @param type The type of modification
     * @param value The value to apply
     * @return A new component with the modification
     */
    public ItemStatsComponent withBaseStat(Statistic stat, StatValueType type, float value) {
        StatProfile newBase = baseStats.copy();
        newBase.addStat(stat, type, value);
        return new ItemStatsComponent(newBase, new ArrayList<>(modifiers));
    }

    /**
     * Returns a new component with the given modifier added
     *
     * @param modifier The stat modifier to add
     * @return A new component with the modifier
     */
    public ItemStatsComponent withModifier(StatModifierComponent modifier) {
        List<StatModifierComponent> newModifiers = new ArrayList<>(modifiers);
        newModifiers.add(modifier);
        return new ItemStatsComponent(baseStats, newModifiers);
    }

    /**
     * Returns a new component with the given type of modifiers removed
     *
     * @param type The type of modifiers to remove
     * @return A new component without the modifiers
     */
    public ItemStatsComponent withoutModifier(ModifierType type) {
        List<StatModifierComponent> newModifiers = new ArrayList<>(modifiers.size());

        for (StatModifierComponent modifier : modifiers) {
            if (modifier.getModifierType() != type) {
                newModifiers.add(modifier);
            }
        }

        return new ItemStatsComponent(baseStats, newModifiers);
    }

    /**
     * Generates lore components for displaying stats
     *
     * @return A list of text components for the item lore
     */
    public List<Component> generateStatLore() {
        List<Component> lore = new ArrayList<>();
        StatProfile stats = getCombinedStats();

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
                .append(Component.text(formattedValue, stat.getDisplayColor()));

        return builder.build();
    }

    /**
     * Calculates the combined stats from base stats and all modifiers
     */
    private StatProfile calculateCombinedStats() {
        StatProfile result = baseStats.copy();

        for (StatModifierComponent modifier : modifiers) {
            result.combineWith(modifier.getStatProfile());
        }

        return result;
    }
}
