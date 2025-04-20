package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.Component;
import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.components.ItemCategoryComponent;
import com.github.unjoinable.skyblock.item.component.components.MaterialComponent;
import com.github.unjoinable.skyblock.item.component.components.NameComponent;
import com.github.unjoinable.skyblock.item.component.components.RarityComponent;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.github.unjoinable.skyblock.item.enums.Rarity;
import com.github.unjoinable.skyblock.stats.StatProfile;
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

    public static class Builder {
        private String id = null;
        private Material material = Material.AIR;
        private String name = null;
        private Rarity rarity = Rarity.UNOBTAINABLE;
        private ItemCategory category = ItemCategory.NONE;
        private StatProfile statistics = null;

        private ComponentContainer container = new ComponentContainer();
        private List<net.kyori.adventure.text.Component> description = null;

        public Builder() {
        } //default constructor for gson

        public SkyblockItem build() {
            container.with(new MaterialComponent(material));
            container.with(new NameComponent(name));
            container.with(new RarityComponent(rarity, false));
            container.with(new ItemCategoryComponent(category));
            return new SkyblockItem(id, container);
        }
    }
}
