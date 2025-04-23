package com.github.unjoinable.skyblock.item.component.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;

/**
 * A Gson {@link TypeAdapter} for serializing and deserializing {@link Component} objects
 * using MiniMessage format.
 * This adapter serializes a {@link Component} into its MiniMessage string representation
 * and deserializes a MiniMessage string back into a {@link Component}. It also
 * specifically removes the {@link TextDecoration#ITALIC} decoration upon deserialization.
 */
public class ComponentAdapter extends TypeAdapter<Component> {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();


    @Override
    public void write(JsonWriter out, Component value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(miniMessage.serialize(value));
    }


    @Override
    public Component read(JsonReader in) throws IOException {
        String str = in.nextString();
        if (str == null || str.isEmpty()) {
            return Component.empty();
        }
        // Deserialize and explicitly remove the italic decoration
        return miniMessage.deserialize(str).decoration(TextDecoration.ITALIC, false);
    }
}