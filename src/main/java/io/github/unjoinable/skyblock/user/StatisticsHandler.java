package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.components.StatisticsComponent;
import io.github.unjoinable.skyblock.statistics.StatModifier;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.util.NamespacedId;

import java.util.*;


public class StatisticsHandler {
    private final SkyblockPlayer player;
    private final Map<Statistic, List<StatModifier>> statModifiers;
    private final Map<SkyblockItem, Map<Statistic, Integer>> itemStatistics;
    private final Map<Statistic, Integer> statistics;

    //current
    private int health;
    private int mana;

    public StatisticsHandler(SkyblockPlayer player) {
        this.player = player;
        this.statModifiers = new EnumMap<>(Statistic.class);
        this.statistics = new EnumMap<>(Statistic.class);
        this.itemStatistics = new HashMap<>();
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

    /**public void tick() {
        update();

        //natural health regeneration
        int healthRegen = this.statistics.get(Statistic.HEALTH_REGEN);
        int maxHealth = this.statistics.get(Statistic.HEALTH);
        int healthGain = (int) ((1.5 + (double) maxHealth /100) * healthRegen/100);

        if (healthGain + this.health > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health += healthGain;
        }

        //natural mana regeneration
        int intelligence = this.statistics.get(Statistic.INTELLIGENCE);
        int manaGain = (int) (intelligence * 0.02);

        if (this.mana + manaGain > intelligence) {
            this.mana = intelligence;
        } else {
            this.mana += manaGain;
        }

    }

    public void update() {
        updateItemStats();
        addAll();
    }

    public void updateItemStats() {
        this.itemStatistics.clear();
        for (SkyblockItem item : PlayerItemCache.fromCache(player).getAll().values()) {
            if (!item.id().equalsIgnoreCase("AIR") && item.hasComponent(StatisticsComponent.class)) {
                StatisticsComponent component = item.getComponent(StatisticsComponent.class);
                this.itemStatistics.put(item, component.statistics());
            }
        }
    }

    public void addAll() {
        Map<Statistic, Integer> overall = new EnumMap<>(Statistic.class);
        for (Statistic statistic : Statistic.getValues()) {
            List<StatModifier> modifiers = statModifiers.get(statistic);
            int additiveValue = 0;
            int multiplicativeValue = 1;
            int baseValue = statistic.getBaseValue();

            if (itemStatistics != null && !itemStatistics.isEmpty()) {
                for (Map<Statistic, Integer> map : itemStatistics.values()) {
                    if (map.containsKey(statistic)) {
                        baseValue += map.get(statistic);
                    }}}
            if (modifiers != null && !modifiers.isEmpty()) {
                for (StatModifier modifier : modifiers) {
                    switch (modifier.type()) {
                        case ADDITIVE ->  additiveValue += modifier.value();
                        case MULTIPLICATIVE ->  multiplicativeValue *= modifier.value();
                    }
                }}
            overall.put(statistic, baseValue*(1 + additiveValue)*multiplicativeValue);
        }
        statistics.putAll(overall);
    }

    public Map<Statistic, Integer> getStatsofItem(SkyblockItem item) {
        Map<Statistic, List<StatModifier>> statModifiers = new EnumMap<>(Statistic.class);
        item.components().values().stream().filter(StatisticalModifier.class::isInstance).forEach(modifier -> {
            ((StatisticalModifier) modifier).getStatModifier().forEach((statistic, statModifier) -> {
                List<StatModifier> modifiers = statModifiers.getOrDefault(statistic, new ArrayList<>());
                modifiers.add(statModifier);
                statModifiers.put(statistic, modifiers);
            });
        });
        return addModifiers(statModifiers);
    }


    public Map<Statistic, Integer> getStatistics() {
        return statistics;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }**/
}
