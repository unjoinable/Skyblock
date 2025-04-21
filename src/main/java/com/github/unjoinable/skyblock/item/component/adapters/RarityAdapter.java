package com.github.unjoinable.skyblock.item.component.adapters;

import com.github.unjoinable.skyblock.item.enums.Rarity;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A Gson {@link TypeAdapter} for serializing and deserializing the {@link Rarity} enum.
 *
 * This adapter serializes a {@link Rarity} enum constant to its name string.
 * It deserializes a string back into a {@link Rarity} enum constant, performing
 * a case-insensitive lookup. If the string does not match any enum constant, it defaults
 * to {@link Rarity#UNOBTAINABLE}.
 */
public class RarityAdapter extends TypeAdapter<Rarity> {
    @Override
    public void write(JsonWriter out, Rarity value) throws IOException {
        out.value(value.name());
    }

    @Override
    public Rarity read(JsonReader in) throws IOException {
        String str = in.nextString();
        try {
            // Deserialize by valueOf, converting to uppercase for case-insensitivity
            return Rarity.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to UNOBTAINABLE if the string doesn't match any enum constant
            return Rarity.UNOBTAINABLE;
        }
    }
}