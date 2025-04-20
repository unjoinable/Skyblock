package com.github.unjoinable.skyblock.item.component.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minestom.server.item.Material;

import java.io.IOException;

public class MaterialAdapter extends TypeAdapter<Material> {
    @Override
    public void write(JsonWriter out, Material value) throws IOException {
        out.value(value.name());
    }

    @Override
    public Material read(JsonReader in) throws IOException {
        String str = in.nextString();
        try {
            return Material.fromKey(str);
        } catch (IllegalArgumentException e) {
            return Material.AIR; // Default fallback
        }
    }
}

