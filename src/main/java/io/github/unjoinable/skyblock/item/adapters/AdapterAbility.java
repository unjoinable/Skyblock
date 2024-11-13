package io.github.unjoinable.skyblock.item.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.github.unjoinable.skyblock.item.ability.Ability;

import java.lang.reflect.Type;

public class AdapterAbility implements JsonDeserializer<Ability> {

    @Override
    public Ability deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return null;
    }
}
