package net.skyblock.item;

import net.skyblock.item.component.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.components.*;
import net.skyblock.item.enums.ItemCategory;
import net.skyblock.item.enums.Rarity;
import net.skyblock.stats.StatProfile;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Optional;

/**
 * Represents an immutable Hypixel Skyblock item with its associated components.
 * The item's properties are stored in a {@link ComponentContainer}.
 *
 * @param itemId     Unique item identifier (e.g., "HYPERION").
 * @param components A thread-safe container holding various {@link Component} instances that define the item's properties.
 */
public record SkyblockItem(String itemId, ComponentContainer components) {
    public static final SkyblockItem AIR = new SkyblockItem("AIR", null);

    /**
     * Retrieves a component of the specified type from the item's component container.
     *
     * @param type The class of the component to retrieve.
     * @param <T> The type of the component.
     * @return An {@link Optional} containing the component if found, otherwise empty.
     */
    public <T extends Component> Optional<T> get(Class<T> type) {
        return components.get(type);
    }

    /**
     * A builder class for constructing {@link SkyblockItem} instances.
     * This class is typically used during JSON deserialization to populate
     * item properties before building the final immutable {@link SkyblockItem}.
     */
    @SuppressWarnings("unused")
    public static class Builder {
        private String id;
        private Material material;
        private String name;
        private Rarity rarity;
        private ItemCategory category;
        private StatProfile statistics;
        private String skin; // Used for head textures

        private ComponentContainer container = new ComponentContainer();
        private List<net.kyori.adventure.text.Component> description;

        /**
         * Private constructor. Instances should be created via deserialization or static methods if added later.
         * Used by Gson during the deserialization process.
         */
        private Builder() {}

        /**
         * Builds the immutable {@link SkyblockItem} instance from the properties set in the builder.
         * Creates and adds standard components (Material, Name, Rarity, Category) and
         * optional components (Stats, Description, HeadTexture).
         *
         * @return The constructed {@link SkyblockItem}.
         */
        public SkyblockItem build() {
            container = container
                    .with(new MaterialComponent(material))
                    .with(new NameComponent(name))
                    .with(new RarityComponent(rarity))
                    .with(new ItemCategoryComponent(category));

            // Add optional components if the corresponding builder fields are set
            if (statistics != null) {
                container = container.with(new StatsComponent(statistics));
            }

            if (description != null && !description.isEmpty()) {
                container = container.with(new DescriptionComponent(description));
            }

            if (skin != null) {
                container = container.with(new HeadTextureComponent(skin));
            }

            return new SkyblockItem(id, container);
        }
    }
}