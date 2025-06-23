package net.unjoinable.item.service;

import net.unjoinable.item.ItemMetadata;
import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.attribute.AttributeContainer;
import net.unjoinable.item.attribute.impls.BaseStatsAttribute;
import net.unjoinable.item.attribute.traits.StatModifierAttribute;
import net.unjoinable.statistic.StatProfile;
import net.unjoinable.statistic.StatValueType;
import org.jetbrains.annotations.NotNull;

/**
 * Utility service for calculating item statistics by combining attributes.
 * <p>
 * This calculator processes an item's attributes in the following order:
 * <ol>
 *   <li>Apply base stats from BaseStatsAttribute if present</li>
 *   <li>Apply all StatModifierAttribute contributions</li>
 * </ol>
 */
public final class ItemStatsCalculator {

    private ItemStatsCalculator() {
        throw new UnsupportedOperationException("ItemStatsCalculator cannot be instantiated");
    }

    /**
     * Computes a complete StatProfile for an item by combining all stat-modifying attributes.
     *
     * @param item The item to calculate stats for
     * @return A new StatProfile containing all stat contributions
     */
    public static @NotNull StatProfile computeItemStats(@NotNull SkyblockItem item) {
        AttributeContainer attributes = item.attributes();
        ItemMetadata metadata = item.metadata();
        StatProfile stats = new StatProfile();

        applyBaseStats(stats, attributes);
        applyStatModifiers(stats, attributes, metadata);

        return stats;
    }

    /**
     * Applies base stats from the BaseStatsAttribute if present.
     *
     * @param stats The StatProfile to update
     * @param attributes The attribute container to extract base stats from
     */
    private static void applyBaseStats(@NotNull StatProfile stats, @NotNull AttributeContainer attributes) {
        attributes.get(BaseStatsAttribute.class)
                .ifPresent(baseStats -> stats.loadFromMap(baseStats.baseStats(), StatValueType.BASE));
    }

    /**
     * Applies all StatModifierAttribute contributions to the StatProfile.
     *
     * @param stats The StatProfile to update
     * @param attributes The attribute container with stat modifiers
     * @param metadata The item metadata for contextual calculations
     */
    private static void applyStatModifiers(
            @NotNull StatProfile stats,
            @NotNull AttributeContainer attributes,
            @NotNull ItemMetadata metadata
    ) {
        attributes.stream()
                .filter(StatModifierAttribute.class::isInstance)
                .map(StatModifierAttribute.class::cast)
                .map(modifier -> modifier.modifierStats(attributes, metadata))
                .forEach(stats::combineWith);
    }
}