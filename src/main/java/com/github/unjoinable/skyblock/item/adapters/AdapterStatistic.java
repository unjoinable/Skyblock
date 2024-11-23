package com.github.unjoinable.skyblock.item.adapters;

import com.google.gson.*;
import com.github.unjoinable.skyblock.statistics.holders.StatModifier;
import com.github.unjoinable.skyblock.statistics.holders.StatModifiers;
import com.github.unjoinable.skyblock.statistics.holders.StatModifiersMap;
import com.github.unjoinable.skyblock.statistics.holders.StatValueType;
import com.github.unjoinable.skyblock.statistics.Statistic;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class AdapterStatistic implements JsonDeserializer<StatModifiersMap> {
    @Override
    public StatModifiersMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Map<Statistic, StatModifiers> statistics = new EnumMap<>(Statistic.class);
        Statistic.getValues().stream().filter(statistic -> obj.has(statistic.name()))
                .forEach(statistic -> {
                    int value = obj.get(statistic.name()).getAsInt();
                    StatModifiers modifiers = new StatModifiers();
                    modifiers.addModifier(new StatModifier(StatValueType.BASE, value));
                    statistics.put(statistic, modifiers);
                });
        StatModifiersMap map = new StatModifiersMap();
        statistics.forEach((stat, value) -> {
            map.put(stat,value);
        });
        return map;
    }
}
