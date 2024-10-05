package io.github.unjoinable.item.adapters;

import com.google.gson.*;
import io.github.unjoinable.statistics.Statistic;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class AdapterStatistic implements JsonDeserializer<Map<Statistic, Integer>> {
    @Override
    public Map<Statistic, Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Map<Statistic, Integer> statistics = new EnumMap<>(Statistic.class);
        Statistic.getValues().stream().filter(statistic -> obj.has(statistic.name())).forEach(statistic -> {
            statistics.put(statistic, obj.get(statistic.name()).getAsInt());
        });
        return statistics;
    }
}
