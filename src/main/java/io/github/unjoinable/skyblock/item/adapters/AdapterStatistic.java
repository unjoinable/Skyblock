package io.github.unjoinable.skyblock.item.adapters;

import com.google.gson.*;
import io.github.unjoinable.skyblock.statistics.StatModifier;
import io.github.unjoinable.skyblock.statistics.StatModifiers;
import io.github.unjoinable.skyblock.statistics.StatValueType;
import io.github.unjoinable.skyblock.statistics.Statistic;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class AdapterStatistic implements JsonDeserializer<Map<Statistic, StatModifiers>> {
    @Override
    public Map<Statistic, StatModifiers> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Map<Statistic, StatModifiers> statistics = new EnumMap<>(Statistic.class);
        Statistic.getValues().stream().filter(statistic -> obj.has(statistic.name()))
                .forEach(statistic -> {
                    int value = obj.get(statistic.name()).getAsInt();
                    StatModifiers modifiers = new StatModifiers();
                    modifiers.addModifier(new StatModifier(StatValueType.BASE, value));
                    statistics.put(statistic, modifiers);
                });
        return statistics;
    }
}
