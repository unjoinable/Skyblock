package io.github.unjoinable.item.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minestom.server.item.Material;

import java.lang.reflect.Type;

public class AdapterMaterial implements JsonDeserializer<Material> {

    @Override
    public Material deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String str = json.getAsString();
        if (str == null) return Material.AIR;
        str = str.toLowerCase();
        Material material = Material.fromNamespaceId(str);
        if (material == null) {
            material = Material.AIR;
        }
        return material;
    }
}
