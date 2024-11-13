package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.components.StatisticsComponent;
import io.github.unjoinable.skyblock.statistics.StatModifiers;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.util.NamespacedId;
import net.minestom.server.entity.attribute.Attribute;

import java.util.*;

/**
 * Handles and manages statistics for a SkyblockPlayer.
 */
public class StatisticsHandler {
    private final SkyblockPlayer player;
    private final Map<SkyblockItem, Map<Statistic, StatModifiers>> itemStatistics;
    private final Map<Statistic, Double> overallStatistics;

    //current
    private double health;
    private double mana;

    /**
     * Constructs a new StatisticsHandler for the given SkyblockPlayer.
     *
     * @param player The SkyblockPlayer associated with this StatisticsHandler.
     */
    public StatisticsHandler(SkyblockPlayer player) {
        this.player = player;
        this.itemStatistics = new HashMap<>();
        this.overallStatistics = new EnumMap<>(Statistic.class);
    }

    /**
     * Updates the statistics for all items in the player's inventory.
     * Clears previous item statistics and recalculates based on current inventory.
     */
    public void updateItemStats() {
        this.itemStatistics.clear();
        for (SkyblockItem item : PlayerItemCache.fromCache(player).getAll().values()) {
            ComponentContainer container = item.container();
            if (!item.id().equals(NamespacedId.AIR) && container.hasComponent(StatisticsComponent.class)) {
                StatisticsComponent component = container.getComponent(StatisticsComponent.class);
                this.itemStatistics.put(item, component.statistics());
            }
        }
    }

    /**
     * Retrieves the value of a specific statistic.
     * By default it is 0
     *
     * @param stat The Statistic to retrieve.
     * @return The current value of the specified statistic.
     */
    public double getStat(Statistic stat) {
        return this.overallStatistics.getOrDefault(stat, 0D);
    }

    /**
     * Updates the overall statistics by combining base values and item stats.
     */
    public void updateOverallStats() {
        this.overallStatistics.clear();

        for (Statistic value : Statistic.getValues()) {
            overallStatistics.put(value, ((double) value.getBaseValue()));
        }

        for (Map<Statistic, StatModifiers> itemStat : itemStatistics.values()) {
            itemStat.forEach((statistic, statModifiers) -> {
                double value = overallStatistics.get(statistic);
                value += statModifiers.getEffectiveValue();
                overallStatistics.put(statistic, value);
            });
        }
    }

    /**
     * Updates both item and overall statistics.
     */
    public void update() {
        updateItemStats();
        updateOverallStats();
    }

    /**
     * Retrieves the map of overall statistics.
     *
     * @return A map of all statistics and their current values.
     */
    public Map<Statistic, Double> getOverallStats() {
        return overallStatistics;
    }

    /**
     * Gets the current health of the player.
     *
     * @return The current health value.
     */
    public double getHealth() {
        return health;
    }

    /**
     * Gets the current mana of the player.
     *
     * @return The current mana value.
     */
    public double getMana() {
        return mana;
    }

    /**
     * Performs periodic updates on player statistics, health, mana, and movement speed.
     * This method should be called regularly to keep the player's stats up-to-date.
     */
    public void taskLoop() {
        update();
        health += calcHealthRegen();
        mana += calcManaRegen();

        //speed
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(overallStatistics.get(Statistic.SPEED) / 1000);
    }

    /**
     * Calculates the amount of health regeneration.
     *
     * @return The amount of health to be regenerated.
     */
    public double calcHealthRegen() {
        double healthRegen = overallStatistics.get(Statistic.HEALTH_REGEN);
        double maxHealth = overallStatistics.get(Statistic.HEALTH);
        double healthGain = ((1.5 + (maxHealth /100)) * (healthRegen/100));

        if (healthGain + this.health > maxHealth) {
            return maxHealth - health;
        } else {
            return healthGain;
        }
    }


    /**
     * Calculates the amount of mana regeneration.
     *
     * @return The amount of mana to be regenerated.
     */
    public double calcManaRegen() {
        double intelligence = overallStatistics.get(Statistic.INTELLIGENCE);
        double manaGain = (intelligence * 0.02);

        if (this.mana + manaGain > intelligence) {
            return intelligence - mana;
        } else {
            return manaGain;
        }
    }

    /**
     * Sets the current health of the player.
     *
     * @param health The new health value to set.
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Sets the current mana of the player.
     *
     * @param mana The new mana value to set.
     */
    public void setMana(double mana) {
        this.mana = mana;
    }
}
