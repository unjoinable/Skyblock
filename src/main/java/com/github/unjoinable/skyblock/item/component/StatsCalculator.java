package com.github.unjoinable.skyblock.item.component;

import com.github.unjoinable.skyblock.item.StatModifierManager;
import com.github.unjoinable.skyblock.item.component.components.BaseStatsComponent;
import com.github.unjoinable.skyblock.stats.StatProfile;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class that calculates the combined stats for an item.
 */
public final class StatsCalculator {

    /**
     * Calculates the combined stats for an item based on its components.
     * @param container The component container to calculate stats for
     * @return A StatProfile containing all combined stats
     */
    public @NotNull StatProfile calculateCombinedStats(@NotNull ComponentContainer container) {
        // Get base stats
        StatProfile baseStats = container.get(BaseStatsComponent.class)
                .map(BaseStatsComponent::getBaseStats)
                .orElseGet(() -> new StatProfile(false));

        // Apply modifiers if present
        return container.get(StatModifierManager.class)
                .map(manager -> manager.applyModifiers(baseStats))
                .orElse(baseStats);
    }
}
