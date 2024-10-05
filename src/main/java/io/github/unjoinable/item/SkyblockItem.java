package io.github.unjoinable.item;

import com.google.gson.annotations.SerializedName;
import io.github.unjoinable.Skyblock;
import io.github.unjoinable.enums.ItemCategory;
import io.github.unjoinable.enums.Rarity;
import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.item.component.components.*;
import io.github.unjoinable.statistics.Statistic;
import io.github.unjoinable.util.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record SkyblockItem(@NotNull String id, @NotNull Map<Class<? extends ItemComponent>, ItemComponent> components) {

    public static SkyblockItem AIR = new SkyblockItem("AIR", Map.of());

    public Builder builder() {
        return new Builder(this);
    }

    public boolean hasComponent(@NotNull Class<? extends ItemComponent> component) {
        return components.containsKey(component);
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent> T getComponent(@NotNull Class<T> component) {
        return (T) components.get(component);
    }

    public ItemStack toItemStack() {
       return Skyblock.getItemProcessor().toItemStack(this);
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public <T extends ItemComponent> T getOrDefault(@NotNull Class<T> componentType) {
        if (this.hasComponent(componentType)) {
            return (T) this.components.get(componentType);
        } else {
            try {
                ItemComponent component = componentType.newInstance();
                return (T) component.defaultComponent();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Builder {
        private String id = "AIR";
        private Material material = Material.AIR;
        private String name = "null";
        private @SerializedName("tier") Rarity rarity = Rarity.UNOBTAINABLE;
        private ItemCategory category = ItemCategory.NONE;
        private @SerializedName("stats") Map<Statistic, Integer> statistics = new EnumMap<>(Statistic.class);
        private String skin = null;
        private String color = null;
        private @SerializedName("npc_sell_price") int sellPrice = -1;
        private @SerializedName("dungeon_item") boolean isDungeonItem = false;

        private Map<Class<? extends ItemComponent>, ItemComponent> components = new HashMap<>();
        private List<Component> description = new ArrayList<>();


        public Builder(SkyblockItem item) {
            this.id = item.id;
            this.components = item.components;
            this.material = item.getOrDefault(MaterialComponent.class).material();
            this.name = item.getOrDefault(NameComponent.class).name();
            this.category = item.getOrDefault(ItemCategoryComponent.class).category();
            this.statistics = item.getOrDefault(StatisticsComponent.class).statistics();
            this.skin = item.getOrDefault(ArmorSkinComponent.class).texture();
            this.color = Arrays.toString(item.getOrDefault(ArmorColorComponent.class).color());
            this.sellPrice = item.getOrDefault(NpcSellPriceComponent.class).price();
            this.isDungeonItem = item.getOrDefault(DungeonComponent.class).isDungeonItem();
            this.rarity = item.getOrDefault(RarityComponent.class).rarity();
        }

        public Builder() {
        }

        public Builder setCategory(ItemCategory category) {
            if (category == null) throw new IllegalArgumentException("category cannot be null");
            this.category = category;
            return this;
        }

        public Builder setMaterial(Material material) {
            if (material == null) throw new IllegalArgumentException("material cannot be null");
            this.material = material;
            return this;
        }

        public Builder setName(@Nullable String name) {
            this.name = name;
            return this;
        }

        public Builder setSellPrice(int sellPrice) {
            if (sellPrice < -1) sellPrice = -1;
            this.sellPrice = sellPrice;
            return this;
        }

        public Builder setRarity(Rarity rarity) {
            if (rarity == null) throw new IllegalArgumentException("rarity cannot be null");
            this.rarity = rarity;
            return this;
        }

        public Builder setId(String id) {
            if (id == null) throw new IllegalArgumentException("id cannot be null");
            this.id = id;
            return this;
        }

        public Builder setStatistics(Map<Statistic, Integer> statistics) {
            this.statistics = statistics;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setSkin(String skin) {
            this.skin = skin;
            return this;
        }

        public Builder setDescription(List<Component> description) {
            this.description = description;
            return this;
        }

        public Builder addComponent(ItemComponent component) {
            this.components.put(component.getClass(), component);
            return this;
        }

        public SkyblockItem build() {
            //components
            if (!description.isEmpty()) this.addComponent(new DescriptionComponent(description));
            if (id.equalsIgnoreCase("AIR")) return SkyblockItem.AIR;
            if (skin != null) this.addComponent(new ArmorSkinComponent(skin));
            if (color != null) this.addComponent(new ArmorColorComponent(Utils.strArtoIntArray(color)));
            if (isDungeonItem) this.addComponent(new DungeonComponent(true));
            if (statistics != null) this.addComponent(new StatisticsComponent(statistics));
            this.addComponent(new NpcSellPriceComponent(sellPrice));
            this.addComponent(new ItemCategoryComponent(category));
            this.addComponent(new MaterialComponent(material));
            this.addComponent(new NameComponent(name));
            this.addComponent(new RarityComponent(rarity));

            //components which require a bit of code to register.
            if (!components.containsKey(UUIDComponent.class)) {
                if (UUIDComponent.canHasUUID(category)) this.addComponent(new UUIDComponent(UUID.randomUUID()));
            }

            //build
            return new SkyblockItem(id, components);
        }
    }
}


