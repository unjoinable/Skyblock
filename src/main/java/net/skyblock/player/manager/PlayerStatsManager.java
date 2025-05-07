package net.skyblock.player.manager;

import net.skyblock.item.component.definition.StatsComponent;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.inventory.ItemSlot;
import net.skyblock.item.inventory.PlayerItemProvider;
import net.skyblock.item.inventory.VanillaItemSlot;
import net.skyblock.player.SkyblockPlayer;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages and calculates player stats from equipped items.
 * Provides efficient caching and targeted slot updates to minimize performance impact.
 * Uses a unified equipment slot system that covers both vanilla and custom slots.
 * Also handles health and mana regeneration calculations.
 */
public class PlayerStatsManager {
    private final SkyblockPlayer player;
    private final PlayerItemProvider itemProvider;
    private final StatProfile baseStats; // Base stats (from player level, skills, etc.)
    private final Map<ItemSlot, StatProfile> itemStats; // All equipment stats
    private StatProfile cachedCombinedProfile; // Combined cached profile (all sources)
    private boolean isDirty;

    // Constants for regeneration calculations
    private static final double BASE_HEALTH_REGEN_CONST = 1.5;
    private static final double BASE_MANA_REGEN_CONST = 0.02;

    /**
     * Creates a new stats manager for the specified player.
     *
     * @param player The player whose stats are being managed
     * @param itemProvider the provider for player items
     */
    public PlayerStatsManager(@NotNull SkyblockPlayer player, @NotNull PlayerItemProvider itemProvider) {
        this.player = player;
        this.itemProvider = itemProvider;
        this.baseStats = new StatProfile(true);
        this.itemStats = new HashMap<>();

        for (VanillaItemSlot slot: VanillaItemSlot.values()) {
            itemStats.put(slot, new StatProfile());
        }

        this.cachedCombinedProfile = new StatProfile();
        this.isDirty = true;

        // Initial calculation of all equipment stats
        recalculateAll();
    }

    /**
     * Gets the complete stat profile for the player, combining base stats and all equipment.
     * Uses cached values when available for better performance.
     *
     * @return The complete player stat profile
     */
    @NotNull
    public StatProfile getStatProfile() {
        if (isDirty) {
            rebuildCachedProfile();
        }
        return cachedCombinedProfile;
    }

    /**
     * Updates stats for a specific item slot (unified system).
     *
     * @param slot The item slot that changed
     */
    public void update(@NotNull ItemSlot slot) {
        SkyblockItem item = itemProvider.getItem(slot);
        StatProfile profile = new StatProfile();

        if (item != null && item.components() != null) {
            Optional<StatsComponent> statsComponent = item.components().get(StatsComponent.class);
            if (statsComponent.isPresent()) {
                profile = null;// TODO: statsComponent.get().getFinalStats(item.components());
            }
        }

        itemStats.put(slot, profile);
        isDirty = true;
    }

    /**
     * Recalculates stats for all equipment slots.
     * This is more expensive than targeted updates but useful for initialization
     * or when multiple pieces of equipment change at once.
     */
    public void recalculateAll() {
        for (VanillaItemSlot slot : VanillaItemSlot.values()) {
            update(slot);
        }

        isDirty = true;
    }

    /**
     * Updates the base stats (non-equipment stats) for the player.
     * These could come from skills, levels, etc.
     *
     * @param newBaseStats The new base stats to use
     */
    public void updateBaseStats(@NotNull StatProfile newBaseStats) {
        this.baseStats.combineWith(newBaseStats);
        isDirty = true;
    }

    /**
     * Rebuilds the cached combined profile from all stat sources.
     * Optimized to efficiently combine all stat sources.
     * Now uses a freshly created StatProfile for cleaner combination.
     */
    private void rebuildCachedProfile() {
        // Create a fresh profile and copy the base stats first
        StatProfile combined = baseStats.copy();

        // Add stats from all equipment slots
        for (StatProfile slotStats : this.itemStats.values()) {
            combined.combineWith(slotStats);
        }

        this.cachedCombinedProfile = combined;
        this.isDirty = false;
    }

    /**
     * Calculates how much health should regenerate per tick;
     *
     * @return the amount of health to regenerate
     */
    public double calculateHealthRegeneration() {
        double healthRegenStat = getStatProfile().get(Statistic.HEALTH_REGEN);
        double maxHealth = player.getMaxHealth();

        return (BASE_HEALTH_REGEN_CONST + (maxHealth / 100.0)) * (healthRegenStat / 100.0);
    }

    /**
     * Calculates how much mana should regenerate per tick
     *
     * @return the amount of mana to regenerate
     */
    public double calculateManaRegeneration() {
        return player.getCurrentMana() * BASE_MANA_REGEN_CONST;
    }
}