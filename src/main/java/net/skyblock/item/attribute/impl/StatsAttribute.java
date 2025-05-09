package net.skyblock.item.attribute.impl;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.ItemLoreAttribute;
import net.skyblock.item.attribute.base.JsonAttribute;
import net.skyblock.item.attribute.base.StatModifierAttribute;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.StatValueType;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.skyblock.utils.Utils.formatStatValue;

/**
 * Manages item statistics and their modifiers in the Skyblock implementation.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Storing and managing an item's base statistics</li>
 *   <li>Handling stat modifiers that affect the base values</li>
 *   <li>Calculating final stat values</li>
 *   <li>Formatting stat lines for item lore display</li>
 * </ul>
 */
public record StatsAttribute(@NotNull Map<Statistic, Double> baseStats, @NotNull List<StatModifierAttribute> modifiers)
        implements ItemLoreAttribute, JsonAttribute {

    public static final String ID = "statistics";
    public static final Codec<StatsAttribute> CODEC = StructCodec.struct(
            "baseStats", Codec.Enum(Statistic.class).mapValue(Codec.DOUBLE), StatsAttribute::baseStats,
            StatsAttribute::new
    );

    /**
     * Creates a StatsAttribute with empty base statistics and no modifiers.
     */
    public StatsAttribute() {
        this(new EnumMap<>(Statistic.class), Collections.emptyList());
    }

    /**
     * Creates a StatsAttribute with the specified base statistics and no modifiers.
     *
     * @param baseStats a map of base statistics to their values
     */
    public StatsAttribute(Map<Statistic, Double> baseStats) {
        this(baseStats, Collections.emptyList());
    }

    /**
     * Returns the unique identifier string for the statistics attribute type.
     *
     * @return the attribute identifier "statistics"
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    /**
     * Returns the codec used for serializing and deserializing this attribute.
     *
     * @return the codec for this attribute type
     */
    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }

    /**
     * Returns the display priority for this attribute in item lore.
     * <p>
     * A lower value means higher priority; this attribute always has the highest priority (0).
     *
     * @return 0, indicating the highest display priority
     */
    @Override
    public int priority() {
        return 0;
    }

    /**
     * Returns a list of formatted text components representing the item's non-zero statistics for display in item lore.
     *
     * Only statistics with values other than zero are included in the output. Each line is formatted for visual presentation, incorporating any relevant modifiers.
     *
     * @param container the attribute container providing context for stat calculation
     * @return a list of formatted components for each non-zero statistic
     */
    @Override
    public @NotNull List<Component> loreLines(@NotNull AttributeContainer container) {
        var stats = getFinalStats(container);
        return Statistic.getValues().stream()
                .filter(stat -> stats.get(stat) != 0)
                .map(stat -> formatStat(stat, stats.get(stat), container))
                .toList();
    }

    /**
     * Returns a StatProfile representing the combined statistics from base values and all modifiers.
     *
     * @param container the attribute container providing context for modifier calculation
     * @return a StatProfile with all modifiers applied to the base stats
     */
    public @NotNull StatProfile getFinalStats(@NotNull AttributeContainer container) {
        var result = StatProfile.fromMap(baseStats, StatValueType.BASE);
        modifiers.forEach(mod -> result.combineWith(mod.getStats(container)));
        return result;
    }

    /**
     * Returns a formatted text component representing a statistic and its value for item lore, including any non-zero modifier indicators.
     *
     * @param stat the statistic to display
     * @param value the base value of the statistic
     * @param container the attribute container providing context for modifiers
     * @return a non-italicized text component showing the stat name, value, and any applicable modifiers
     */
    private @NotNull Component formatStat(@NotNull Statistic stat, double value, @NotNull AttributeContainer container) {
        Component result = textOfChildren(
                text(stat.getDisplayName() + ": ", GRAY),
                text(formatStatValue(value, stat), stat.getLoreColor())).decoration(ITALIC, false);

        for (StatModifierAttribute mod : modifiers) {
            double modValue = mod.getStats(container).get(stat);
            if (modValue != 0) {
                result = textOfChildren(result, text(" "), mod.getFormattedDisplay(stat, modValue));
            }
        }
        return result;
    }

    /**
     * Returns a new StatsAttribute instance with the specified modifier added.
     *
     * @param mod the stat modifier to include
     * @return a new StatsAttribute containing all existing modifiers and the added one
     */
    public @NotNull StatsAttribute with(@NotNull StatModifierAttribute mod) {
        var newMods = new ArrayList<>(modifiers);
        newMods.add(mod);
        return new StatsAttribute(baseStats, newMods);
    }

    /**
     * Returns a new StatsAttribute instance with the specified modifier removed from the modifiers list.
     *
     * @param mod the modifier to remove
     * @return a new StatsAttribute without the given modifier
     */
    public @NotNull StatsAttribute without(@NotNull StatModifierAttribute mod) {
        return new StatsAttribute(baseStats, modifiers.stream().filter(m -> !m.equals(mod)).toList()
        );
    }

    /**
     * Returns an unmodifiable list of stat modifiers applied to the base statistics.
     *
     * @return an unmodifiable list of StatModifierAttribute instances
     */
    public @NotNull List<StatModifierAttribute> modifiers() {
        return Collections.unmodifiableList(modifiers);
    }
}