package net.skyblock.player.manager;

import net.skyblock.item.attribute.impl.StatsAttribute;
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

/**
 * Manages player statistics derived from multiple sources including equipment, skills, and base stats.
 * Features efficient caching with targeted updates to minimize performance impact during gameplay.
 *
 * <p><b>Key features:</b></p>
 * <ul>
 *   <li>Equipment stats calculation with slot-specific updates</li>
 *   <li>Stat caching with lazy recalculation</li>
 *   <li>Health and mana regeneration formulas</li>
 *   <li>Direct access to specific stats</li>
 * </ul>
 */
public class PlayerStatsManager {
    private final SkyblockPlayer player;
    private final PlayerItemProvider itemProvider;
    private final StatProfile baseStats;
    private final Map<ItemSlot, StatProfile> itemStats;
    private StatProfile cachedProfile;
    private boolean isDirty;

    // Regeneration constants
    private static final double BASE_HEALTH_REGEN = 1.5;
    private static final double BASE_MANA_REGEN = 0.02;

    /**
     * Creates a new stats manager for the specified player.
     *
     * @param player the player whose stats are managed
     * @param itemProvider provides access to the player's equipped items
     */
    public PlayerStatsManager(@NotNull SkyblockPlayer player, @NotNull PlayerItemProvider itemProvider) {
        this.player = player;
        this.itemProvider = itemProvider;
        this.baseStats = new StatProfile().createDefaultProfile();
        this.itemStats = new HashMap<>();
        this.cachedProfile = new StatProfile();
        this.isDirty = true;

        // Initialize stat profiles for all vanilla slots
        for (VanillaItemSlot slot : VanillaItemSlot.values()) {
            itemStats.put(slot, new StatProfile());
        }
    }

    /**
     * Gets the complete stat profile for the player.
     * Uses cached values when available for better performance.
     *
     * @return the complete player stat profile
     */
    @NotNull
    public StatProfile getStatProfile() {
        if (isDirty) {
            rebuildCachedProfile();
        }
        return cachedProfile;
    }

    /**
     * Gets a specific statistic value from the player's profile.
     *
     * @param stat the statistic to retrieve
     * @return the value of the requested statistic
     */
    public double getStat(@NotNull Statistic stat) {
        return getStatProfile().get(stat);
    }

    /**
     * Updates the stat profile for a specific equipment slot.
     *
     * @param slot the equipment slot to update
     */
    public void update(@NotNull ItemSlot slot) {
        itemProvider.updateSlot(slot);
        SkyblockItem item = itemProvider.getItem(slot);
        StatProfile profile = new StatProfile();

        if (item != null) {
            item.attributes().get(StatsAttribute.class).ifPresent(attribute ->
                    profile.combineWith(attribute.getFinalStats(item.attributes()))
            );
        }

        itemStats.put(slot, profile);
        isDirty = true;
    }

    /**
     * Recalculates stats for all equipment slots.
     */
    public void recalculateAll() {
        for (VanillaItemSlot slot : VanillaItemSlot.values()) {
            update(slot);
        }
        isDirty = true;
    }

    /**
     * Calculates health regeneration per tick.
     *
     * @return amount of health to regenerate
     */
    public double calculateHealthRegeneration() {
        double healthRegenStat = getStat(Statistic.HEALTH_REGEN);
        double maxHealth = player.getMaxHealth();

        return (BASE_HEALTH_REGEN + (maxHealth / 100.0)) * (healthRegenStat / 100.0);
    }

    /**
     * Calculates mana regeneration per tick.
     *
     * @return amount of mana to regenerate
     */
    public double calculateManaRegeneration() {
        return player.getMaxMana() * BASE_MANA_REGEN;
    }

    /**
     * Rebuilds the cached combined profile from all stat sources.
     */
    private void rebuildCachedProfile() {
        StatProfile combined = baseStats.copy();

        // Add stats from all equipment slots
        itemStats.values().forEach(combined::combineWith);

        this.cachedProfile = combined;
        this.isDirty = false;
    }
}