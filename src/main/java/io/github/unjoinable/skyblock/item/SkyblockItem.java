package io.github.unjoinable.skyblock.item;

import com.google.gson.annotations.SerializedName;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.Component;
import io.github.unjoinable.skyblock.item.component.ComponentContainer;
import io.github.unjoinable.skyblock.item.component.components.*;
import io.github.unjoinable.skyblock.registry.registries.ItemRegistry;
import io.github.unjoinable.skyblock.statistics.holders.StatModifiers;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.statistics.holders.StatModifiersMap;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import io.github.unjoinable.skyblock.util.NamespacedObject;
import io.github.unjoinable.skyblock.util.Utils;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record SkyblockItem(NamespacedId id, ComponentContainer container) implements NamespacedObject {

    public static final SkyblockItem AIR = new SkyblockItem(NamespacedId.AIR, new ComponentContainer());

    private static final Tag<String> ID_TAG = Tag.String("id");

    public @NotNull ItemStack toItemStack(@Nullable SkyblockPlayer player) {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR);
        Map<Class<? extends Component>, Component> components = container().getAllComponents();

        for (io.github.unjoinable.skyblock.item.component.Component component : components.values()) {
            if (component instanceof BasicComponent) {
                ((BasicComponent) component).applyEffect(this, builder);
            }
        }

        builder.set(ID_TAG, id.toString());
        builder.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        builder.set(ItemComponent.ATTRIBUTE_MODIFIERS, new AttributeList(List.of(), false));
        builder.lore(new LoreSystem(this, player).build());
        return builder.build();
    }

    public static @NotNull SkyblockItem fromItemStack(@NotNull ItemStack item) {
        if (!item.hasTag(ID_TAG)) return SkyblockItem.AIR;
        SkyblockItem skyblockItem = ItemRegistry.getInstance().getItem(item.getTag(ID_TAG));
        // TODO: Load item from item stack
        return skyblockItem;
    }

    public static class Builder {
        private NamespacedId id = NamespacedId.AIR;
        private Material material = Material.AIR;
        private String name = null;
        private @SerializedName("tier") Rarity rarity = Rarity.UNOBTAINABLE;
        private ItemCategory category = ItemCategory.NONE;
        private @SerializedName("stats") StatModifiersMap statistics = null;
        private String skin = null;
        private String color = null;
        private @SerializedName("npc_sell_price") int sellPrice = -1;
        private @SerializedName("dungeon_item") boolean isDungeonItem = false;
        private Ability ability = null;

        private ComponentContainer container = new ComponentContainer();
        private List<net.kyori.adventure.text.Component> description = null;

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

            if (container.hasComponent(AbilityComponent.class)) {
                ability = container.getComponent(AbilityComponent.class).ability();
            }
        }

        public SkyblockItem build() {
            container.addComponent(new MaterialComponent(material));
            container.addComponent(new NameComponent(name));
            container.addComponent(new RarityComponent(rarity));
            container.addComponent(new ItemCategoryComponent(category));
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

            if (statistics != null) {
                container.addComponent(new StatisticsComponent(statistics));
            }

            if (ability!= null) {
                container.addComponent(new AbilityComponent(ability));
            }

            if (sellPrice != 0) {
                container.addComponent(new NpcSellPriceComponent(sellPrice));
            }
            return new SkyblockItem(id, container);
        }
    }
 }

