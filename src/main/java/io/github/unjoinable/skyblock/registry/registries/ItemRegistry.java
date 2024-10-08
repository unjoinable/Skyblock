package io.github.unjoinable.skyblock.registry.registries;

import com.google.gson.*;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.adapters.AdapterDescription;
import io.github.unjoinable.skyblock.item.adapters.AdapterMaterial;
import io.github.unjoinable.skyblock.item.adapters.AdapterStatistic;
import io.github.unjoinable.skyblock.registry.Registry;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemRegistry extends Registry<String, SkyblockItem> {
    private static final ItemRegistry INSTANCE = new ItemRegistry();
    public static final JsonObject ITEMS_JSON;
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Material.class, new AdapterMaterial())
            .registerTypeAdapter(Map.class, new AdapterStatistic())
            .registerTypeAdapter(List.class, new AdapterDescription())
            .create();

    static {
        try {
            ITEMS_JSON = GSON.fromJson(new FileReader("skyblock-items.txt"), JsonObject.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerAll() {
        JsonArray items = ITEMS_JSON.getAsJsonArray("items");
        for (JsonElement item : items) {
            SkyblockItem skyblockItem = GSON.fromJson(item, SkyblockItem.Builder.class).build();
            this.add(skyblockItem.id(), skyblockItem);
        }
        System.out.println(STR."Registered \{this.getAllObjects().size()} items");

    }

    @Override
    public @NotNull Iterator<SkyblockItem> iterator() {
        return this.getAllObjects().values().iterator();
    }

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

}
