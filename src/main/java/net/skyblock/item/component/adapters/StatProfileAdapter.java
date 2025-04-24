package net.skyblock.item.component.adapters;

import net.skyblock.stats.StatProfile;
import net.skyblock.stats.StatValueType;
import net.skyblock.stats.Statistic;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * A Gson {@link TypeAdapter} for serializing and deserializing {@link StatProfile} objects.
 *
 * This adapter serializes a {@link StatProfile} into a JSON object where keys are
 * the names of the {@link Statistic} enum constants and values are the base stat values
 * from the profile. Only stats with a non-zero base value are included in the output JSON.
 *
 * Deserialization reads a JSON object, mapping keys (statistic names) to base stat values.
 * Unknown statistic names encountered during deserialization are skipped. The deserialized
 * values are added to a new {@link StatProfile} as {@link StatValueType#BASE} stats.
 */
public class StatProfileAdapter extends TypeAdapter<StatProfile> {

    @Override
    public void write(JsonWriter out, StatProfile value) throws IOException {
        out.beginObject();
        for (Statistic stat : Statistic.values()) {
            float base = value.get(stat);
            // Only write non-zero base stats
            if (base != 0f) {
                out.name(stat.name()).value(base);
            }
        }
        out.endObject();
    }

    @Override
    public StatProfile read(JsonReader in) throws IOException {
        // Create a new profile without initializing default base stats
        StatProfile profile = new StatProfile(false);
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            try {
                // Find the corresponding Statistic enum by name
                Statistic stat = Statistic.valueOf(name);
                float val = (float) in.nextDouble();
                // Add the read value as a base stat
                profile.addStat(stat, StatValueType.BASE, val);
            } catch (IllegalArgumentException e) {
                // If the stat name is unknown, skip its value
                in.skipValue();
            }
        }
        in.endObject();
        return profile;
    }
}