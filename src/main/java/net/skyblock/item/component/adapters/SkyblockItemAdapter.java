package net.skyblock.item.component.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.skyblock.item.SkyblockItem;
import net.skyblock.item.SkyblockItemBuilder;

import java.lang.reflect.Type;

/**
 * A Gson {@link JsonDeserializer} for deserializing {@link SkyblockItem} objects.
 * This adapter works by first deserializing the JSON element into a {@link SkyblockItemBuilder}
 * and then calling the {@code build()} method on the builder to construct the final
 * {@link SkyblockItem} instance. This is a common pattern for deserializing complex
 * objects that are best created via a builder.
 */
public class SkyblockItemAdapter implements JsonDeserializer<SkyblockItem> {

    @Override
    public SkyblockItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        SkyblockItemBuilder builder = context.deserialize(json, SkyblockItemBuilder.class);
        return builder.build();
    }
}