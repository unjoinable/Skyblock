package com.github.unjoinable.skyblock.item.component.adapters;

import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A Gson {@link TypeAdapter} for serializing and deserializing the {@link ItemCategory} enum.
 *
 * This adapter serializes an {@link ItemCategory} enum constant to its name string.
 * It deserializes a string back into an {@link ItemCategory} enum constant, performing
 * a case-insensitive lookup. If the string does not match any enum constant, it defaults
 * to {@link ItemCategory#NONE}.
 */
public class ItemCategoryAdapter extends TypeAdapter<ItemCategory> {
    @Override
    public void write(JsonWriter out, ItemCategory value) throws IOException {
        out.value(value.name());
    }

    @Override
    public ItemCategory read(JsonReader in) throws IOException {
        String str = in.nextString();
        try {
            return ItemCategory.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ItemCategory.NONE;
        }
    }
}