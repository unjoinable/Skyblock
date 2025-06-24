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
    private static final double BASE_HEALTH_REGEN_CONST = 1.5;
    private static final double BASE_MANA_REGEN_CONST = 0.02;

    private final SkyblockPlayer player;
    private final ItemProcessor itemProcessor;

    private final StatProfile baseStats;
    private final Map<ItemSlot, StatProfile> cachedItemStats;
    private final StatProfile cachedFinalStats;

    private boolean isDirty;
    private boolean isInitialized;

    private double currentHealth;
    private double currentMana;
    private boolean invulnerable;

    /**
     * Constructs a new PlayerStatSystem for the specified player.
     *
     * @param player the SkyblockPlayer this system belongs to
     * @param itemProcessor the processor used to handle item operations
     */
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
     * <p>
     * This method recalculates the stats for the item in the specified slot
     * and marks the system as dirty, requiring a recalculation of final stats.
     *
     * @param slot the item slot to update
     * @throws IllegalStateException if the system has not been initialized
     */
    public void updateSlot(@NotNull ItemSlot slot) {
        if (!this.isInitialized) throw new IllegalStateException("PlayerStatSystem has not been initialized");

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
        this.isInitialized = true;

        for (VanillaItemSlot slot : VanillaItemSlot.values()) {
            updateSlot(slot);
        }
    }

    @Override
    public void shutdown() {
        this.cachedItemStats.clear();
        this.isInitialized = false;
    }

    /**
     * Returns the player's final stats, recalculating if dirty.
     * <p>
     * This method combines base stats with all equipped item stats to provide
     * the player's current total statistics. The result is cached and only
     * recalculated when the system is marked as dirty.
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
     * <p>
     * This method resets the cached final stats and combines them with
     * base stats and all item stats from equipped items. After recalculation,
     * the system is marked as clean (not dirty).
     */
    private void recalculateFinalStats() {
        this.cachedFinalStats.reset();
        this.cachedFinalStats.combineWith(this.baseStats);

        for (StatProfile itemStat : this.cachedItemStats.values()) {
            this.cachedFinalStats.combineWith(itemStat);
        }

        this.isDirty = false;
    }

    /**
     * Attempts to consume the specified amount of mana from the player's current mana pool.
     * <p>
     * If the player has sufficient mana, it will be deducted and the method returns true.
     * Otherwise, no mana is consumed and the method returns false.
     *
     * @param amount the amount of mana to consume
     * @return true if the mana was successfully consumed, false if insufficient mana
     */
    public boolean consumeMana(double amount) {
        if (currentMana >= amount) {
            setCurrentMana(currentMana - amount);
            return true;
        }
        return false;
    }

    /**
     * Gets the player's current intelligence stat, which determines maximum mana.
     *
     * @return the current intelligence value
     */
    public double getIntelligence() {
        return getStat(Statistic.INTELLIGENCE);
    }

    /**
     * Gets the player's maximum health based on their current health stat.
     *
     * @return the maximum health value
     */
    public double getMaxHealth() {
        return getStat(Statistic.HEALTH);
    }

    /**
     * Gets the player's current health points.
     * <p>
     * This represents the actual health remaining, not the maximum possible health.
     *
     * @return the current health value
     */
    public double getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets the player's current mana points.
     * <p>
     * This represents the actual mana remaining, not the maximum possible mana.
     *
     * @return the current mana value
     */
    public double getCurrentMana() {
        return currentMana;
    }

    /**
     * Checks if the player is currently invulnerable to damage.
     *
     * @return true if the player is invulnerable, false otherwise
     */
    public boolean isInvulnerable() {
        return invulnerable;
    }

    /**
     * Sets the player's current health, clamping it between 0 and maximum health.
     * <p>
     * This method automatically updates the underlying Minecraft player's health bar
     * and will kill the player if health reaches 0.
     *
     * @param health the new health value to set
     */
    public void setCurrentHealth(double health) {
        double maxHealth = getMaxHealth();
        this.currentHealth = Math.clamp(health, 0.0D, maxHealth);

        double healthPercentage = this.currentHealth / maxHealth;
        this.player.setHealth((float) (healthPercentage * 40.0)); // 40 hp or 20 hearts

        if (this.currentHealth <= 0.0) {
            player.kill();
        }
    }

    /**
     * Sets the player's current mana, clamping it between 0 and maximum mana (intelligence).
     *
     * @param mana the new mana value to set
     */
    public void setCurrentMana(double mana) {
        double maxMana = getIntelligence();
        this.currentMana = Math.clamp(mana, 0.0D, maxMana);
    }

    /**
     * Sets the player's invulnerability status.
     * <p>
     * When invulnerable, the player should not take damage from most sources.
     *
     * @param invulnerable true to make the player invulnerable, false to make them vulnerable
     */
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    /**
     * Calculates how much health should regenerate every 2 seconds
     *
     * @return the amount of health to regenerate
     */
    public double calculateHealthRegeneration() {
        double healthRegenStat = getStat(Statistic.HEALTH_REGEN);
        double maxHealth = getMaxHealth();

        return (BASE_HEALTH_REGEN_CONST + (maxHealth / 100.0)) * (healthRegenStat / 100.0);
    }

    /**
     * Calculates how much mana should regenerate per tick
     *
     * @return the amount of mana to regenerate
     */
    public double calculateManaRegeneration() {
        return getIntelligence() * BASE_MANA_REGEN_CONST;
    }

    /**
     * Regenerates player health based on stats.
     * This should be called from a scheduled task.
     */
    public void regenerateHealth() {
        if (player.isDead()) return;

        double regenAmount = calculateHealthRegeneration();
        setCurrentHealth(getCurrentHealth() + regenAmount);
    }

    /**
     * Regenerates player mana based on stats.
     * This should be called from a scheduled task.
     */
    public void regenerateMana() {
        if (player.isDead()) return;

        double regenAmount = calculateManaRegeneration();
        setCurrentMana(getCurrentMana() + regenAmount);
    }

    /**
     * Resets the player's health and mana to their maximum values.
     * Useful when respawning or initializing the player.
     */
    public void resetHealthAndMana() {
        this.currentHealth = getMaxHealth();
        this.currentMana = getIntelligence();
    }
}