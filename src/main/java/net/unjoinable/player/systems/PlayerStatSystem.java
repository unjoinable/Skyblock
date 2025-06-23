package net.unjoinable.player.systems;

import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.service.ItemProcessor;
import net.unjoinable.item.service.ItemStatsCalculator;
import net.unjoinable.player.PlayerSystem;
import net.unjoinable.player.SkyblockPlayer;
import net.unjoinable.player.ui.inventory.ItemSlot;
import net.unjoinable.player.ui.inventory.VanillaItemSlot;
import net.unjoinable.statistic.StatProfile;
import net.unjoinable.statistic.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles player statistics by calculating and caching stats
 * based on equipped items and base values.
 */
public class PlayerStatSystem implements PlayerSystem {

    private final SkyblockPlayer player;
    private final ItemProcessor itemProcessor;

    private final StatProfile baseStats;
    private final Map<ItemSlot, StatProfile> cachedItemStats;
    private final StatProfile cachedFinalStats;

    private boolean isDirty;
    private boolean isInitialized;

    public PlayerStatSystem(@NotNull SkyblockPlayer player, @NotNull ItemProcessor itemProcessor) {
        this.player = player;
        this.itemProcessor = itemProcessor;

        this.baseStats = StatProfile.createDefaultProfile();
        this.cachedItemStats = new HashMap<>();
        this.cachedFinalStats = new StatProfile();
        this.isDirty = true;
        this.isInitialized = false;
    }

    /**
     * Updates the cached stats for the given item slot.
     *
     * @param slot the item slot to update
     */
    public void updateSlot(@NotNull ItemSlot slot) {
        if (!this.isInitialized) throw  new IllegalStateException("PlayerStatSystem has not been initialized");

        SkyblockItem item = slot.getItem(this.player, this.itemProcessor);
        StatProfile itemStats = ItemStatsCalculator.computeItemStats(item);
        this.cachedItemStats.put(slot, itemStats);
        this.isDirty = true;
    }

    @Override
    public boolean isInitialized() {
        return this.isInitialized;
    }

    @Override
    public void start() {
        for (VanillaItemSlot slot : VanillaItemSlot.values()) {
            this.updateSlot(slot);
        }
        this.isInitialized = true;
    }

    @Override
    public void shutdown() {
        this.cachedItemStats.clear();
        this.isInitialized = false;
    }

    /**
     * Returns the player's final stats, recalculating if dirty.
     *
     * @return combined base + item stats
     */
    public StatProfile getFinalStats() {
        if (this.isDirty) {
            this.recalculateFinalStats();
        }
        return this.cachedFinalStats;
    }

    /**
     * Retrieves the value of a specific statistic from the player's final stats.
     * <p>
     * If the cached final stats are dirty (outdated), they will be recalculated first.
     *
     * @param stat the statistic to retrieve (e.g. HEALTH, STRENGTH, CRIT_DAMAGE)
     * @return the current value of the specified statistic
     */
    public double getStat(@NotNull Statistic stat) {
        return this.getFinalStats().get(stat);
    }

    /**
     * Recalculates the final stats by combining base and item stats.
     */
    private void recalculateFinalStats() {
        this.cachedFinalStats.reset();
        this.cachedFinalStats.combineWith(this.baseStats);

        for (StatProfile itemStat : this.cachedItemStats.values()) {
            this.cachedFinalStats.combineWith(itemStat);
        }

        this.isDirty = false;
    }
}
