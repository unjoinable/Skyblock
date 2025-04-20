package com.github.unjoinable.skyblock.item.component.adapters;

import com.github.unjoinable.skyblock.item.enums.Rarity;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class RarityAdapter extends TypeAdapter<Rarity> {
    @Override
    public void write(JsonWriter out, Rarity value) throws IOException {
        out.value(value.name());
    }

    @Override
    public Rarity read(JsonReader in) throws IOException {
        String str = in.nextString();
        try {
            return Rarity.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Rarity.UNOBTAINABLE;
        }
    }
}

