package com.github.unjoinable.skyblock.item.component.adapters;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * A Gson {@link JsonDeserializer} for deserializing {@link SkyblockItem} objects.
 * This adapter works by first deserializing the JSON element into a {@link SkyblockItem.Builder}
 * and then calling the {@code build()} method on the builder to construct the final
 * {@link SkyblockItem} instance. This is a common pattern for deserializing complex
 * objects that are best created via a builder.
 */
public class SkyblockItemAdapter implements JsonDeserializer<SkyblockItem> {

    @Override
    public SkyblockItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        SkyblockItem.Builder builder = context.deserialize(json, SkyblockItem.Builder.class);
        return builder.build();
    }
}