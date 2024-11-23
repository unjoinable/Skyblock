package com.github.unjoinable.skyblock.item.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.github.unjoinable.skyblock.util.NamespacedId;

import java.lang.reflect.Type;

public class AdapterId implements JsonDeserializer<NamespacedId> {
    @Override
    public NamespacedId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamespacedId.fromSkyblockNamespace(json.getAsString());
    }
}
