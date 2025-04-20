package com.github.unjoinable.skyblock.item.component.adapters;

import com.github.unjoinable.skyblock.stats.StatProfile;
import com.github.unjoinable.skyblock.stats.StatValueType;
import com.github.unjoinable.skyblock.stats.Statistic;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class StatProfileAdapter extends TypeAdapter<StatProfile> {

    @Override
    public void write(JsonWriter out, StatProfile value) throws IOException {
        out.beginObject();
        for (Statistic stat : Statistic.values()) {
            float base = value.get(stat);
            if (base != 0f) {
                out.name(stat.name()).value(base);
            }
        }
        out.endObject();
    }

    @Override
    public StatProfile read(JsonReader in) throws IOException {
        StatProfile profile = new StatProfile(false); // No base stat init
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            try {
                Statistic stat = Statistic.valueOf(name);
                float val = (float) in.nextDouble();
                profile.addStat(stat, StatValueType.BASE, val);
            } catch (IllegalArgumentException e) {
                in.skipValue(); // Skip unknown stats
            }
        }
        in.endObject();
        return profile;
    }
}

