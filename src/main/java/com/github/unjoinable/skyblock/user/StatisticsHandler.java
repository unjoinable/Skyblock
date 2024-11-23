package com.github.unjoinable.skyblock.user;

import com.github.unjoinable.skyblock.events.SkyblockStatUpdateEvent;
import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.components.StatisticsComponent;
import com.github.unjoinable.skyblock.statistics.holders.StatModifiers;
import com.github.unjoinable.skyblock.statistics.Statistic;
import com.github.unjoinable.skyblock.statistics.holders.StatModifiersMap;
import com.github.unjoinable.skyblock.statistics.holders.StatValueMap;
import com.github.unjoinable.skyblock.util.NamespacedId;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.EventDispatcher;

import java.util.*;

/**
 * Handles and manages statistics for a SkyblockPlayer.
 */
public class StatisticsHandler {
    private final SkyblockPlayer player;
    private final Map<SkyblockItem, StatModifiersMap> itemStatistics;
    private StatValueMap overallStatistics;

    //current
    private double currentHealth;
    private double mana;

    //misc
    private final StatModifiersMap modifiers;

    /**
     * Constructs a new StatisticsHandler for the given SkyblockPlayer.
     *
     * @param player The SkyblockPlayer associated with this StatisticsHandler.
     */
    public StatisticsHandler(SkyblockPlayer player) {
        this.player = player;
        this.itemStatistics = new HashMap<>();
        this.overallStatistics = new StatValueMap();
        this.modifiers = new StatModifiersMap();

        update();
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
     * By default, it is 0
     *
     * @param stat The Statistic to retrieve.
     * @return The current value of the specified statistic.
     */
    public double getStat(Statistic stat) {
        return this.overallStatistics.get(stat);
    }

    /**
     * Updates the overall statistics by combining base values and item stats.
     */
    public void updateOverallStats() {
        StatValueMap updatedStats = new StatValueMap();

        for (Statistic value : Statistic.getValues()) {
            updatedStats.put(value, value.getBaseValue());
        }

        StatModifiersMap allModifiers = new StatModifiersMap(); //combining all modifiers

        for (StatModifiersMap value : itemStatistics.values()) {
            allModifiers.addAll(value);
        }

        StatModifiers[] modifiers = allModifiers.getAll();

        for (int i = 0; i < modifiers.length; i++) {
            Statistic stat = Statistic.byOrdinal(i);
            StatModifiers statModifiers = modifiers[i];
            double value = updatedStats.get(i);
            if (statModifiers != null) value += statModifiers.getEffectiveValue();
            updatedStats.put(stat, value);
        }

        SkyblockStatUpdateEvent event = new SkyblockStatUpdateEvent(player, updatedStats);
        EventDispatcher.callCancellable(event, () -> this.overallStatistics = updatedStats);
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
    public StatValueMap getOverallStats() {
        return overallStatistics;
    }

    public void healHealth() {
        setCurrentHealth(overallStatistics.get(Statistic.HEALTH));
    }

    public void healMana() {
        setMana(overallStatistics.get(Statistic.INTELLIGENCE));
    }


    /**
     * Gets the current mana of the player.
     *
     * @return The current mana value.
     */
    public double getMana() {
        return mana;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void subtractCurrentHealth(double currentHealth) {
        setCurrentHealth(getCurrentHealth() - currentHealth);
    }

    public void addCurrentHealth(double currentHealth) {
        setCurrentHealth(Math.min(getCurrentHealth() + currentHealth, overallStatistics.get(Statistic.HEALTH)));
    }

    /**
     * Performs periodic updates on player statistics, health, mana, and movement speed.
     * This method should be called regularly to keep the player's stats up-to-date.
     */
    public void taskLoop() {
        update();
        mana += calcManaRegen();

        //health
        double healthRegen = calcHealthRegen();
        if (healthRegen != 0) {
            addCurrentHealth(healthRegen);
        }

        //speed
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(overallStatistics.get(Statistic.SPEED) / 1000);
        //swing range
        player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).setBaseValue(overallStatistics.get(Statistic.SWING_RANGE));
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

        if (healthGain + this.currentHealth > maxHealth) {
            return maxHealth - currentHealth;
        }
        return healthGain;
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
     * Sets the current mana of the player.
     *
     * @param mana The new mana value to set.
     */
    public void setMana(double mana) {
        this.mana = mana;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
        player.setHealth((float) currentHealth);
    }

    public StatModifiersMap getModifiers() {
        return modifiers;
    }
}
