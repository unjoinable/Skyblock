package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.Component;
import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.components.ItemCategoryComponent;
import com.github.unjoinable.skyblock.item.component.components.MaterialComponent;
import com.github.unjoinable.skyblock.item.component.components.NameComponent;
import com.github.unjoinable.skyblock.item.component.components.RarityComponent;
import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.github.unjoinable.skyblock.item.enums.Rarity;
import com.github.unjoinable.skyblock.stats.StatProfile;
import com.google.gson.annotations.SerializedName;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Optional;

/**
 * Skyblock item representation.
 *
 * @param itemId Unique item identifier (non-null, validated)
 * @param components Thread-safe component storage
 */
public record SkyblockItem(String itemId, ComponentContainer components) {

    public <T extends Component> Optional<T> get(Class<T> type) {
        return components.get(type);
    }

    /**
     * Builds an ItemStack from this SkyblockItem
     *
     * @return The Minestom ItemStack
     */
    public ItemStack toItemStack() {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR);

        // Apply all serializable components
        for (Component component : components.asMap().values()) {
            if (component instanceof SerializableComponent serializable) {
                builder = serializable.nbtWriter().apply(builder);
            }
        }

        // Generate and apply lore
        LoreGenerator loreGenerator = new LoreGenerator(this);
        List<net.kyori.adventure.text.Component> lore = loreGenerator.generate();
        if (!lore.isEmpty()) {
            builder.lore(lore);
        }

        return builder.build();
    }

    public static class Builder {
        private String id;
        private Material material;
        private String name;
        private Rarity rarity;
        private ItemCategory category;
        private @SerializedName("stats") StatProfile statistics;

        private final ComponentContainer container = new ComponentContainer();
        private List<net.kyori.adventure.text.Component> description;

        private Builder() {} // Default constructor for GSON


        public void setCategory(ItemCategory category) {
            this.category = category;
        }

        public void setMaterial(Material material) {
            this.material = material;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setRarity(Rarity rarity) {
            this.rarity = rarity;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(List<net.kyori.adventure.text.Component> description) {
            this.description = description;
        }

        public void setStatistics(StatProfile statistics) {
            this.statistics = statistics;
        }

        public SkyblockItem build() {
            container.with(new MaterialComponent(material));
            container.with(new NameComponent(name));
            container.with(new RarityComponent(rarity, false));
            container.with(new ItemCategoryComponent(category));
            System.out.println(statistics);
            return new SkyblockItem(id, container);
        }
    }
}
