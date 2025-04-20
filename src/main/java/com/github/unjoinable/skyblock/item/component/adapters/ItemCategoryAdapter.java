package com.github.unjoinable.skyblock.item.component.adapters;

import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

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

