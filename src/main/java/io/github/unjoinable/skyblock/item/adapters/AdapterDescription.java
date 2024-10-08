package io.github.unjoinable.skyblock.item.adapters;

import com.google.gson.*;
import net.kyori.adventure.text.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdapterDescription implements JsonDeserializer<List<Component>> {
    @Override
    public List<Component> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Component> description = new ArrayList<>();
        JsonArray jsonArray = json.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            description.add(Component.text(element.getAsString()));
        }
        return description;
    }
}
