package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.components.StatisticsComponent;
import io.github.unjoinable.skyblock.statistics.StatModifiers;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.util.NamespacedId;

import java.util.*;


public class StatisticsHandler {
    private final SkyblockPlayer player;
    private final Map<SkyblockItem, Map<Statistic, StatModifiers>> itemStatistics;
    private final Map<Statistic, Double> overallStatistics;

    //current
    private double health;
    private double mana;

    public StatisticsHandler(SkyblockPlayer player) {
        this.player = player;
        this.itemStatistics = new HashMap<>();
        this.overallStatistics = new EnumMap<>(Statistic.class);
    }

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

    public double getStat(Statistic stat) {
        return this.overallStatistics.getOrDefault(stat, 0D);
    }

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

    public void update() {
        updateItemStats();
        updateOverallStats();
    }

    public Map<Statistic, Double> getOverallStats() {
        return overallStatistics;
    }

    public double getHealth() {
        return health;
    }

    public double getMana() {
        return mana;
    }

    public void taskLoop() {
        update();
        health += calcHealthRegen();
        mana += calcManaRegen();
    }

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

    public double calcManaRegen() {
        double intelligence = overallStatistics.get(Statistic.INTELLIGENCE);
        double manaGain = (intelligence * 0.02);

        if (this.mana + manaGain > intelligence) {
            return intelligence - mana;
        } else {
            return manaGain;
        }
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }
}
