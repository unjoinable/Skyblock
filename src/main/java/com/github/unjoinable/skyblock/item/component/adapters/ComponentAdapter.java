package com.github.unjoinable.skyblock.item.component.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;

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
        return miniMessage.deserialize(str);
    }
}
