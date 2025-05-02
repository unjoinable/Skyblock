package net.skyblock.item;

import net.skyblock.item.enums.Rarity;
import net.skyblock.stats.StatProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Represents a reforge that can be applied to items, providing different stats based on item rarity.
 * <p>
 * Each reforge has a unique identifier and a map of stats profiles corresponding to different rarity tiers.
 * When a specific rarity doesn't have defined stats, the system falls back to the closest lower rarity.
 * </p>
 */
public record Reforge(@NotNull String reforgeId, @NotNull Map<Rarity, StatProfile> stats) {

    /**
     * Gets the stats profile for the specified rarity.
     * <p>
     * If stats aren't defined for the given rarity, this method will look for stats
     * of the closest lower rarity. If no lower rarity has defined stats, an empty
     * stats profile is returned.
     * </p>
     *
     * @param rarity the rarity to get stats for
     * @return the appropriate stats profile for the given rarity, never null
     */
    @NotNull
    public StatProfile getStats(Rarity rarity) {
        return Optional.ofNullable(findStatsForRarityOrLower(rarity))
                .orElseGet(StatProfile::new);
    }

    /**
     * Finds stats for the given rarity or falls back to the closest lower rarity with defined stats.
     *
     * @param rarity the rarity to check
     * @return the stats profile or null if no applicable stats were found
     */
    private @Nullable StatProfile findStatsForRarityOrLower(@NotNull Rarity rarity) {
        if (stats.containsKey(rarity)) {
            return stats.get(rarity);
        }

        Rarity currentRarity = rarity;
        while ((currentRarity = currentRarity.degrade()) != rarity) {
            if (stats.containsKey(currentRarity)) {
                return stats.get(currentRarity).copy(); // Defensive Copy
            }
        }

        return null;
    }
}