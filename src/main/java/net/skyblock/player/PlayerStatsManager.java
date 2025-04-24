package net.skyblock.player;

import net.skyblock.item.SkyblockItem;
import net.skyblock.item.component.components.StatsComponent;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;
import net.skyblock.stats.combat.DamageReason;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Manages and calculates player stats from equipped items.
 * Provides efficient caching and targeted slot updates to minimize performance impact.
 * Uses a unified equipment slot system that covers both vanilla and custom slots.
 * Also handles health and mana regeneration calculations.
 */
public class PlayerStatsManager {
    private final SkyblockPlayer player;
    private final StatProfile baseStats; // Base stats (from player level, skills, etc.)
    private final Map<ItemSlot, StatProfile> itemStats; // All equipment stats
    private StatProfile cachedCombinedProfile; // Combined cached profile (all sources)
    private boolean isDirty;

    // Constants for regeneration calculations
    private static final float BASE_HEALTH_REGEN_PERCENT = 1.0f; // 1% base regen per second
    private static final float BASE_MANA_REGEN_PERCENT = 2.0f;   // 2% base regen per second
    private static final float INTELLIGENCE_MANA_BONUS = 0.005f; // 0.5% bonus per intelligence point
    private static final Random RANDOM = new Random();

    /**
     * Creates a new stats manager for the specified player.
     *
     * @param player The player whose stats are being managed
     */
    public PlayerStatsManager(@NotNull SkyblockPlayer player) {
        this.player = player;
        this.baseStats = new StatProfile(true);

        // Initialize all slot stats with capacity to avoid resizing
        this.itemStats = new EnumMap<>(ItemSlot.class);
        for (ItemSlot slot : ItemSlot.values()) {
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
        SkyblockItem item = slot.getItem(player);
        StatProfile profile = new StatProfile();

        if (item != null && item.components() != null) {
            Optional<StatsComponent> statsComponent = item.get(StatsComponent.class);
            if (statsComponent.isPresent()) {
                profile = statsComponent.get().calculateFinalStats(item.components()).copy();
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
        for (ItemSlot slot : ItemSlot.values()) {
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
    public float calculateHealthRegeneration() {
        float healthRegenStat = getStatProfile().get(Statistic.HEALTH_REGEN);
        float maxHealth = player.getMaxHealth();

        return (1.5f + (maxHealth / 100f)) * (healthRegenStat / 100f);
    }

    /**
     * Calculates how much mana should regenerate per tick
     *
     * @return the amount of mana to regenerate
     */
    public float calculateManaRegeneration() {
        float intelligence = getStatProfile().get(Statistic.INTELLIGENCE);
        return player.getCurrentMana() * (BASE_MANA_REGEN_PERCENT / 100f) +
                (intelligence * INTELLIGENCE_MANA_BONUS);
    }

    /**
     * Calculates the damage reduction from defense according to Hypixel Skyblock formula.
     *
     * @param damage the incoming damage amount
     * @return the amount of damage after defense reduction
     */
    public float calculateDefenseReduction(float damage) {
        float defense = getStatProfile().get(Statistic.DEFENSE);
        // Hypixel Skyblock formula: damage * (1 - (defense / (defense + 100)))
        return damage * (1f - (defense / (defense + 100f)));
    }

    /**
     * Calculates the absolute damage that would be dealt based on base damage and strength stats
     * using the Hypixel Skyblock damage formula.
     *
     * @return the calculated damage value
     */
    public float calculateAbsoluteDamage() {
        StatProfile stats = getStatProfile();
        float baseDamage = stats.get(Statistic.DAMAGE);
        float strength = stats.get(Statistic.STRENGTH);

        // Hypixel Skyblock formula: (5 + baseDamage) * (1 + strength / 100)
        return (5f + baseDamage) * (1f + strength / 100f);
    }

    /**
     * Calculates if an attack is a critical hit based on critical chance stat.
     *
     * @return true if the attack is critical, false otherwise
     */
    public boolean rollForCritical() {
        float critChance = getStatProfile().get(Statistic.CRIT_CHANCE);
        return RANDOM.nextFloat() * 100f <= critChance;
    }

    /**
     * Calculates damage multiplier for a critical hit.
     *
     * @return the multiplier for critical hit damage
     */
    public float getCriticalDamageMultiplier() {
        float critDamage = getStatProfile().get(Statistic.CRIT_DAMAGE) / 100f;
        return 1f + critDamage;
    }

    /**
     * Gets the damage multiplier for a specific attack type according to Hypixel Skyblock formulas.
     *
     * @param attackType the type of attack (e.g., MELEE, MAGIC, PROJECTILE)
     * @return the damage multiplier
     */
    public float getDamageMultiplierForType(String attackType) {
        DamageReason reason;
        try {
            reason = DamageReason.valueOf(attackType);
        } catch (IllegalArgumentException e) {
            return 1f; // Default multiplier for unknown types
        }

        return switch (reason) {
            case MELEE -> 1f + (getStatProfile().get(Statistic.STRENGTH) / 100f);
            case MAGIC -> 1f + (getStatProfile().get(Statistic.INTELLIGENCE));
            default -> 1;
        };
    }
}