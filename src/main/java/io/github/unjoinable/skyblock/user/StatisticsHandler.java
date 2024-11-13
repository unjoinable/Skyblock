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
    private final Map<Statistic, Integer> overallStatistics;

    //current
    private int health;
    private int mana;

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

    public int getStat(Statistic stat) {
        return this.overallStatistics.getOrDefault(stat, 0);
    }

    public void updateOverallStats() {
        this.overallStatistics.clear();

        for (Statistic value : Statistic.getValues()) {
            overallStatistics.put(value, value.getBaseValue());
        }

        for (Map<Statistic, StatModifiers> itemStat : itemStatistics.values()) {
            itemStat.forEach((statistic, statModifiers) -> {
                int value = overallStatistics.get(statistic);
                value += statModifiers.getEffectiveValue();
                overallStatistics.put(statistic, value);
            });
        }
    }

    public void update() {
        updateItemStats();
        updateOverallStats();
    }

    public Map<Statistic, Integer> getOverallStats() {
        return overallStatistics;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public void taskLoop() {
        update();
        health += calcHealthRegen();
        mana += calcManaRegen();
    }

    public int calcHealthRegen() {
        int healthRegen = overallStatistics.get(Statistic.HEALTH_REGEN);
        int maxHealth = overallStatistics.get(Statistic.HEALTH);
        int healthGain = (int) ((1.5 + (double) maxHealth /100) * healthRegen/100);

        if (healthGain + this.health > maxHealth) {
            return maxHealth - health;
        } else {
            return healthGain;
        }
    }

    public int calcManaRegen() {
        int intelligence = overallStatistics.get(Statistic.INTELLIGENCE);
        int manaGain = (int) (intelligence * 0.02);

        if (this.mana + manaGain > intelligence) {
            return intelligence - mana;
        } else {
            return manaGain;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }
}
