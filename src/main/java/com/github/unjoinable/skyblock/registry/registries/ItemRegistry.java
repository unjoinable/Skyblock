package com.github.unjoinable.skyblock.registry.registries;

import com.github.unjoinable.skyblock.item.adapters.*;
import com.google.gson.*;
import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.registry.Registry;
import com.github.unjoinable.skyblock.statistics.holders.StatModifiersMap;
import com.github.unjoinable.skyblock.util.NamespacedId;
import com.github.unjoinable.skyblock.util.StringUtils;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class ItemRegistry extends Registry<NamespacedId, SkyblockItem> {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    @Override
    public void registerAll() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Material.class, new AdapterMaterial())
                .registerTypeAdapter(StatModifiersMap.class, new AdapterStatistic())
                .registerTypeAdapter(List.class, new AdapterDescription())
                .registerTypeAdapter(NamespacedId.class, new AdapterId())
                .registerTypeAdapter(Ability.class, new AdapterAbility())
                .create();
        JsonObject itemsJson;

        try {
             itemsJson = gson.fromJson(new FileReader("skyblock-items.txt"), JsonObject.class);
        } catch (FileNotFoundException e) {
            return;
        }

        JsonArray items = itemsJson.getAsJsonArray("items");
        for (JsonElement item : items) {
            SkyblockItem skyblockItem = gson.fromJson(item, SkyblockItem.Builder.class).build();
            this.add(skyblockItem.id(), skyblockItem);
       }
       System.out.println(StringUtils.formatString("Registered {} skyblock items in the registry", this.getAllObjects().size()));
    }

    // getters
    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    public @NotNull SkyblockItem getItem(@NotNull NamespacedId id) {
        return this.get(id);
    }

    public @NotNull SkyblockItem getItem(@NotNull String id) {
        return this.getItem(NamespacedId.fromString(id));
    }

}
