package com.github.unjoinable.skyblock.registry;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.component.adapters.ItemCategoryAdapter;
import com.github.unjoinable.skyblock.item.component.adapters.MaterialAdapter;
import com.github.unjoinable.skyblock.item.component.adapters.RarityAdapter;
import com.github.unjoinable.skyblock.item.component.adapters.StatProfileAdapter;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.github.unjoinable.skyblock.item.enums.Rarity;
import com.github.unjoinable.skyblock.stats.StatProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minestom.server.item.Material;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class ItemRegistry extends Registry<String, SkyblockItem> {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatProfile.class, new StatProfileAdapter())
            .registerTypeAdapter(Rarity.class, new RarityAdapter())
            .registerTypeAdapter(ItemCategory.class, new ItemCategoryAdapter())
            .registerTypeAdapter(Material.class, new MaterialAdapter())
            .create();

    private static final File ITEM_FILE = new File("skyblock_items.json");

    @Override
    public void init() {
        try (FileReader reader = new FileReader(ITEM_FILE)) {
            Type listType = new TypeToken<List<SkyblockItem.Builder>>(){}.getType();
            List<SkyblockItem.Builder> items = gson.fromJson(reader, listType);

            for (SkyblockItem.Builder item : items) {
                SkyblockItem sItem = item.build();
                register(sItem.itemId(), sItem);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load and register Skyblock items", e);
        }
    }
}
