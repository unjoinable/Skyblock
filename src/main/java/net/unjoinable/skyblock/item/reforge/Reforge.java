package net.unjoinable.skyblock.item.reforge;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.unjoinable.skyblock.item.enums.Rarity;
import net.unjoinable.skyblock.combat.statistic.Statistic;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public record Reforge(Key key, Map<Rarity, Map<Statistic, Double>> statistics) implements Keyed {

    private @Nullable Map<Statistic, Double> findStatsForRarityOrLower(Rarity rarity) {
        if (statistics.containsKey(rarity)) {
            return statistics.get(rarity);
        }

        Rarity currentRarity = rarity;
        while ((currentRarity = currentRarity.degrade()) != rarity) {
            if (statistics.containsKey(currentRarity)) {
                return statistics.get(currentRarity);
            }
        }

        return null;
    }

}
