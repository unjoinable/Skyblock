package net.unjoinable.skyblock.item.service;

import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.impls.BaseStatsAttribute;
import net.unjoinable.skyblock.item.attribute.traits.StatModifierAttribute;
import net.unjoinable.skyblock.combat.statistic.StatProfile;
import net.unjoinable.skyblock.combat.statistic.StatValueType;

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
     * Convenience method to compute stats directly from a {@link SkyblockItem}.
     *
     * @param item The item to compute stats for
     * @return A new {@link StatProfile} with the computed stats
     */
    public static StatProfile computeItemStats(SkyblockItem item) {
        return computeItemStats(item.attributes(), item.metadata());
    }

    /**
     * Computes a complete {@link StatProfile} for an item by combining all stat-modifying attributes and metadata.
     *
     * @param attributes The attribute container holding base and modifier attributes
     * @param metadata   The item's metadata, which may influence stat modifiers
     * @return A new {@link StatProfile} containing the calculated stats
     */
    public static StatProfile computeItemStats(AttributeContainer attributes, ItemMetadata metadata) {
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
    private static void applyBaseStats(StatProfile stats, AttributeContainer attributes) {
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
            StatProfile stats,
            AttributeContainer attributes,
            ItemMetadata metadata
    ) {
        attributes.stream()
                .filter(StatModifierAttribute.class::isInstance)
                .map(StatModifierAttribute.class::cast)
                .map(modifier -> modifier.modifierStats(attributes, metadata))
                .forEach(stats::combineWith);
    }
}