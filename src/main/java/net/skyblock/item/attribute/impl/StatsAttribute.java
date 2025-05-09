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

    public StatsAttribute() {
        this(new EnumMap<>(Statistic.class), Collections.emptyList());
    }

    public StatsAttribute(Map<Statistic, Double> baseStats) {
        this(baseStats, Collections.emptyList());
    }

    /**
     * Returns the unique identifier for this attribute type.
     *
     * @return The string "stats" as the attribute identifier
     */
    @Override
    public @NotNull String id() {
        return ID;
    }

    @Override
    public @NotNull Codec<? extends ItemAttribute> getCodec() {
        return CODEC;
    }

    /**
     * Defines the display priority of this attribute in item lore.
     * Lower values indicate higher priority for display ordering.
     *
     * @return An integer value of 0, indicating high priority
     */
    @Override
    public int priority() {
        return 0;
    }

    /**
     * Generates formatted lore lines for displaying the item's statistics.
     * Only statistics with non-zero values will be included in the result.
     *
     * @param container The attribute container context
     * @return A list of formatted text components representing stat lines for display in item lore
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
     * Calculates the final statistics after applying all modifiers.
     * Creates a copy of the base stats and applies each modifier sequentially.
     *
     * @param container The attribute container context
     * @return A new StatProfile containing the combined stats from base values and all modifiers
     */
    public @NotNull StatProfile getFinalStats(@NotNull AttributeContainer container) {
        var result = StatProfile.fromMap(baseStats, StatValueType.BASE);
        modifiers.forEach(mod -> result.combineWith(mod.getStats(container)));
        return result;
    }

    /**
     * Formats a single statistic for display in item lore, including any modifier indicators.
     *
     * @param stat The statistic to format
     * @param value The value of the statistic
     * @param container The attribute container context
     * @return A formatted text component for the stat, including any modifier indicators
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
     * Creates a new StatsAttribute with an additional modifier.
     * This method maintains immutability by creating a new instance with the updated modifiers list.
     *
     * @param mod The modifier to add
     * @return A new StatsAttribute instance with the additional modifier
     */
    public @NotNull StatsAttribute with(@NotNull StatModifierAttribute mod) {
        var newMods = new ArrayList<>(modifiers);
        newMods.add(mod);
        return new StatsAttribute(baseStats, newMods);
    }

    /**
     * Creates a new StatsAttribute with the specified modifier removed.
     * This method maintains immutability by creating a new instance with the updated modifiers list.
     *
     * @param mod The modifier to remove
     * @return A new StatsAttribute instance without the specified modifier
     */
    public @NotNull StatsAttribute without(@NotNull StatModifierAttribute mod) {
        return new StatsAttribute(baseStats, modifiers.stream().filter(m -> !m.equals(mod)).toList()
        );
    }

    /**
     * Returns an unmodifiable view of the modifiers list.
     * This method is auto-generated by the record but is redocumented here for clarity.
     *
     * @return An unmodifiable list of StatModifierAttribute instances
     */
    public @NotNull List<StatModifierAttribute> modifiers() {
        return Collections.unmodifiableList(modifiers);
    }
}
