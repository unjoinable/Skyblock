package net.skyblock.item.component.components;

import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ModifierInfo;
import net.skyblock.item.component.trait.LoreComponent;
import net.skyblock.item.component.trait.StatModifierComponent;
import net.skyblock.item.enums.ModifierType;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;
import net.skyblock.stats.StatValueType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Master component for managing item stats, including base stats, modifiers, and lore generation.
 */
public final class StatsComponent implements LoreComponent {
    private final StatProfile baseStats;
    private final ObjectList<StatModifierComponent> modifiers;

    // Add a cache for the final stats calculation
    private transient StatProfile finalStatsCache;
    private transient boolean statsCacheValid = false;

    /**
     * Creates a new StatsComponent with default empty stats
     */
    public StatsComponent() {
        this(new StatProfile(false));
    }

    /**
     * Creates a new StatsComponent with specified base stats
     *
     * @param baseStats The base stats for this item
     */
    public StatsComponent(@NotNull StatProfile baseStats) {
        this(baseStats, new ObjectArrayList<>());
    }

    /**
     * Creates a new StatsComponent with specified base stats and modifiers
     *
     * @param baseStats The base stats for this item
     * @param modifiers The stat modifiers to apply
     */
    public StatsComponent(@NotNull StatProfile baseStats, @NotNull List<StatModifierComponent> modifiers) {
        this.baseStats = Objects.requireNonNull(baseStats);
        this.modifiers = new ObjectArrayList<>(modifiers);
    }

    /**
     * Gets the base stats of the item without modifiers
     *
     * @return The base stat profile
     */
    public @NotNull StatProfile getBaseStats() {
        return baseStats;
    }

    /**
     * Gets an unmodifiable list of all stat modifiers
     *
     * @return List of stat modifiers
     */
    public @NotNull List<StatModifierComponent> getModifiers() {
        return List.copyOf(modifiers);
    }

    /**
     * Returns a new component with the given base stat added or modified
     *
     * @param stat  The statistic to modify
     * @param value The value to set/add
     * @return A new component with the modification
     */
    public StatsComponent withBaseStat(@NotNull Statistic stat, float value) {
        return withBaseStat(stat, StatValueType.BASE, value);
    }

    /**
     * Returns a new component with the given base stat added or modified
     *
     * @param stat  The statistic to modify
     * @param type  The type of modification
     * @param value The value to apply
     * @return A new component with the modification
     */
    public StatsComponent withBaseStat(@NotNull Statistic stat, @NotNull StatValueType type, float value) {
        StatProfile newBase = baseStats.copy();
        newBase.addStat(stat, type, value);
        return new StatsComponent(newBase, modifiers);
    }

    /**
     * Returns a new component with the given modifier added
     *
     * @param modifier The stat modifier to add
     * @return A new component with the modifier
     */
    public StatsComponent withModifier(@NotNull StatModifierComponent modifier) {
        ObjectList<StatModifierComponent> newModifiers = new ObjectArrayList<>(modifiers);
        newModifiers.add(modifier);
        return new StatsComponent(baseStats, newModifiers);
    }

    /**
     * Returns a new component with the given type of modifiers removed
     *
     * @param type The type of modifiers to remove
     * @return A new component without the modifiers
     */
    public StatsComponent withoutModifier(@NotNull ModifierType type) {
        ObjectList<StatModifierComponent> newModifiers = new ObjectArrayList<>(modifiers);
        newModifiers.removeIf(modifier -> modifier.getModifierType() == type);
        return new StatsComponent(baseStats, newModifiers);
    }

    /**
     * Calculates the final stats with all modifiers applied
     *
     * @return The combined stat profile
     */
    public @NotNull StatProfile calculateFinalStats(ComponentContainer container) {
        if (statsCacheValid && finalStatsCache != null) {
            return finalStatsCache;
        }

        StatProfile result = baseStats.copy();

        for (StatModifierComponent modifier : modifiers) {
            result.combineWith(modifier.getStatProfile(container));
        }

        finalStatsCache = result;
        statsCacheValid = true;
        return result;
    }

    /**
     * Calculates stats and returns info about all modifiers for each stat
     *
     * @return Map of statistics to their modifier information
     */
    public @NotNull Map<Statistic, List<ModifierInfo>> getStatModifierInfo(ComponentContainer container) {
        Object2ObjectOpenHashMap<Statistic, ObjectList<ModifierInfo>> result = new Object2ObjectOpenHashMap<>();

        // Process base stats and modifiers in one loop
        for (Statistic stat : Statistic.getValues()) {
            // Check if this stat has a base value
            if (baseStats.get(stat) != 0) {
                result.put(stat, new ObjectArrayList<>());
                continue;
            }

            // Check if any modifier affects this stat
            boolean hasModifier = false;
            for (StatModifierComponent modifier : modifiers) {
                if (modifier.getStatProfile(container).get(stat) != 0) {
                    hasModifier = true;
                    break;
                }
            }

            if (hasModifier) {
                result.put(stat, new ObjectArrayList<>());
            }
        }

        // Add modifier info for each stat
        for (StatModifierComponent modifier : modifiers) {
            StatProfile modProfile = modifier.getStatProfile(container);

            for (Statistic stat : Statistic.getValues()) {
                float modValue = modProfile.get(stat);
                if (modValue != 0) {
                    ObjectList<ModifierInfo> modList = result.computeIfAbsent(stat, _ -> new ObjectArrayList<>());
                    modList.add(new ModifierInfo(
                            modifier.getModifierType(),
                            modValue,
                            modifier.getOpenBracket(),
                            modifier.getCloseBracket(),
                            modifier.getModifierColor()
                    ));
                }
            }
        }

        // Convert ObjectLists to standard Lists for API compatibility
        Map<Statistic, List<ModifierInfo>> standardResult = new HashMap<>();
        for (Map.Entry<Statistic, ObjectList<ModifierInfo>> entry : result.entrySet()) {
            standardResult.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return standardResult;
    }

    @Override
    public int lorePriority() {
        return 0;
    }

    @Override
    public @NotNull List<Component> generateLore(@NotNull ComponentContainer container) {
        StatProfile finalStats = calculateFinalStats(container);
        Map<Statistic, List<ModifierInfo>> modifierInfo = getStatModifierInfo(container);

        List<Component> lore = new ArrayList<>();

        for (Statistic stat : Statistic.getValues()) {
            float value = finalStats.get(stat);
            if (value > 0) {
                lore.add(formatStatLineWithModifiers(
                        stat,
                        value,
                        modifierInfo.getOrDefault(stat, Collections.emptyList())
                ));
            }
        }

        return lore;
    }

    /**
     * Creates a formatted text component for a single stat line with modifiers
     */
    private Component formatStatLineWithModifiers(
            Statistic stat,
            float value,
            List<ModifierInfo> modifiers) {

        boolean isPercentage = stat.getPercentage();
        String valueFormat = isPercentage ? "%.1f%%" : "%.0f";
        String formattedValue = String.format(valueFormat, value);
        String percentSign = isPercentage ? "%" : "";

        TextComponent.Builder builder = Component.text()
                .append(Component.text(stat.getDisplayName() + ": ", NamedTextColor.GRAY))
                .append(Component.text("+" + formattedValue + percentSign, stat.getLoreColor()))
                .decoration(TextDecoration.ITALIC, false);

        // Add each modifier with its bracket type and color
        for (ModifierInfo mod : modifiers) {
            String modValueText = isPercentage
                    ? String.format("%.1f%%", mod.getValue())
                    : String.format("%.0f", mod.getValue());

            builder.append(Component.text(" ")).append();

            builder.append(Component
                    .text(" " + mod.getOpenBracket() + "+" + modValueText + mod.getCloseBracket(), mod.getColor()));
        }

        return builder.build();
    }
}