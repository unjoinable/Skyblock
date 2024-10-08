package io.github.unjoinable.skyblock.item;

import com.google.gson.annotations.SerializedName;
import io.github.unjoinable.skyblock.Skyblock;
import io.github.unjoinable.skyblock.enums.ItemCategory;
import io.github.unjoinable.skyblock.enums.Rarity;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.components.*;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.*;

public record SkyblockItem(String id, ComponentContainer container) {

    public static SkyblockItem AIR = new SkyblockItem("AIR", null);

    public ItemStack toItemStack() {
        return Skyblock.getItemProcessor().toItemStack(this);
    }


    public Builder builder() {
        return new Builder(this);
    }

    public static class Builder {
        private String id = "AIR";
        private Material material = Material.AIR;
        private String name = null;
        private @SerializedName("tier") Rarity rarity = Rarity.UNOBTAINABLE;
        private ItemCategory category = ItemCategory.NONE;
        private @SerializedName("stats") Map<Statistic, Integer> statistics = null;
        private String skin = null;
        private String color = null;
        private @SerializedName("npc_sell_price") int sellPrice = -1;
        private @SerializedName("dungeon_item") boolean isDungeonItem = false;

        private ComponentContainer container = new ComponentContainer();
        private List<Component> description = null;

        public Builder() {} //default constructor for gson

        public Builder(SkyblockItem item) {
            this.id = item.id;
            this.container = item.container;
            material = container.getComponent(MaterialComponent.class).material();
            name = container.getComponent(NameComponent.class).name();
            rarity = container.getComponent(RarityComponent.class).rarity();
            category = container.getComponent(ItemCategoryComponent.class).category();
            sellPrice = container.getComponent(NpcSellPriceComponent.class).price();
            isDungeonItem = container.getComponent(DungeonItemComponent.class).isDungeonItem();
            description = container.getComponent(DescriptionComponent.class).description();

            //conditional components
            if (container.hasComponent(HelmetSkinComponent.class)) {
                skin = container.getComponent(HelmetSkinComponent.class).texture();
            }

            if (container.hasComponent(ArmorColorComponent.class)) {
                color = Arrays.toString(container.getComponent(ArmorColorComponent.class).color());
            }

            if (container.hasComponent(DescriptionComponent.class)) {
                description = container.getComponent(DescriptionComponent.class).description();
            }

            if (container.hasComponent(StatisticsComponent.class)) {
                statistics = container.getComponent(StatisticsComponent.class).statistics();
            }
        }

        public SkyblockItem build() {
            container.addComponent(new MaterialComponent(material));
            container.addComponent(new NameComponent(name));
            container.addComponent(new RarityComponent(rarity));
            container.addComponent(new ItemCategoryComponent(category));
            container.addComponent(new NpcSellPriceComponent(sellPrice));
            container.addComponent(new DungeonItemComponent(isDungeonItem));

            //conditional components
            if (color != null) {
                container.addComponent(new ArmorColorComponent(Utils.strArtoIntArray(color)));
            }

            if (skin != null) {
                container.addComponent(new HelmetSkinComponent(skin));
            }

            if (description != null && !description.isEmpty()) {
                container.addComponent(new DescriptionComponent(description));
            }

            if (statistics != null && !statistics.isEmpty()) {
                container.addComponent(new StatisticsComponent(statistics));
            }

            container.getAllComponents().values().stream()
                    .filter(BasicComponent.class::isInstance).map(BasicComponent.class::cast)
                    .forEach(component -> component.applyData(container));
            //done
            return new SkyblockItem(id, container);
        }
    }
}
