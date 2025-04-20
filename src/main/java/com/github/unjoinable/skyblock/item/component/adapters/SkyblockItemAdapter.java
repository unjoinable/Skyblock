package com.github.unjoinable.skyblock.item.component.adapters;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SkyblockItemAdapter implements JsonDeserializer<SkyblockItem> {

    @Override
    public SkyblockItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        SkyblockItem.Builder builder = context.deserialize(json, SkyblockItem.Builder.class);
        return builder.build();
    }
}

