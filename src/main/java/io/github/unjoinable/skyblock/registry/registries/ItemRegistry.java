package io.github.unjoinable.skyblock.registry.registries;

import com.google.gson.*;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.adapters.AdapterDescription;
import io.github.unjoinable.skyblock.item.adapters.AdapterId;
import io.github.unjoinable.skyblock.item.adapters.AdapterMaterial;
import io.github.unjoinable.skyblock.item.adapters.AdapterStatistic;
import io.github.unjoinable.skyblock.registry.Registry;
import io.github.unjoinable.skyblock.util.NamespacedId;
import io.github.unjoinable.skyblock.util.StringUtils;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class ItemRegistry extends Registry<NamespacedId, SkyblockItem> {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    @Override
    public void registerAll() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Material.class, new AdapterMaterial())
                .registerTypeAdapter(Map.class, new AdapterStatistic())
                .registerTypeAdapter(List.class, new AdapterDescription())
                .registerTypeAdapter(NamespacedId.class, new AdapterId())
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
