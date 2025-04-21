package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.Component;
import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.components.*;
import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import com.github.unjoinable.skyblock.item.enums.ItemCategory;
import com.github.unjoinable.skyblock.item.enums.Rarity;
import com.github.unjoinable.skyblock.stats.StatProfile;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList; // Not used in this snippet but kept if needed elsewhere
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;

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
    private static final Tag<String> ID_TAG = Tag.String("id").defaultValue("AIR");

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
     * Builds a Minestom {@link ItemStack} representation of this SkyblockItem.
     * Applies serializable components to NBT, generates and adds lore,
     * hides additional tooltips, sets item attributes (currently empty),
     * and sets the custom item ID tag.
     *
     * @return The constructed Minestom ItemStack.
     */
    public ItemStack toItemStack() {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR); // Material is set by a component

        // Apply all serializable components to the ItemStack builder
        for (Component component : components.asMap().values()) {
            if (component instanceof SerializableComponent serializable) {
                serializable.nbtWriter(builder);
            }
        }

        // Generate and apply lore from relevant components
        LoreGenerator loreGenerator = new LoreGenerator(this);
        List<net.kyori.adventure.text.Component> lore = loreGenerator.generate();
        if (!lore.isEmpty()) {
            builder.lore(lore);
        }

        builder.set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        builder.set(ItemComponent.ATTRIBUTE_MODIFIERS, new AttributeList(List.of(), false));
        // Set the custom Skyblock item ID tag
        builder.set(ID_TAG, itemId);

        return builder.build();
    }

    /**
     * A builder class for constructing {@link SkyblockItem} instances.
     * This class is typically used during JSON deserialization to populate
     * item properties before building the final immutable {@link SkyblockItem}.
     */
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
         * Sets the item's category.
         * @param category The {@link ItemCategory}.
         */
        public void setCategory(ItemCategory category) {
            this.category = category;
        }

        /**
         * Sets the item's base material.
         * @param material The Minestom {@link Material}.
         */
        public void setMaterial(Material material) {
            this.material = material;
        }

        /**
         * Sets the item's unique identifier.
         * @param id The item ID string.
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Sets the item's rarity.
         * @param rarity The {@link Rarity}.
         */
        public void setRarity(Rarity rarity) {
            this.rarity = rarity;
        }

        /**
         * Sets the item's display name component.
         * @param name The display name string (MiniMessage or plain).
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Sets the item's description (lore lines).
         * @param description A list of Adventure {@link net.kyori.adventure.text.Component} representing lore lines.
         */
        public void setDescription(List<net.kyori.adventure.text.Component> description) {
            this.description = description;
        }

        /**
         * Sets the item's statistics profile.
         * @param statistics The {@link StatProfile}.
         */
        public void setStatistics(StatProfile statistics) {
            this.statistics = statistics;
        }

        /**
         * Sets the base64 texture string for head items.
         * @param skin The base64 texture string.
         */
        public void setSkin(String skin) {
            this.skin = skin;
        }

        /**
         * Builds the immutable {@link SkyblockItem} instance from the properties set in the builder.
         * Creates and adds standard components (Material, Name, Rarity, Category) and
         * optional components (Stats, Description, HeadTexture).
         *
         * @return The constructed {@link SkyblockItem}.
         */
        public SkyblockItem build() {
            // Add standard components that should always be present based on builder fields
            container = container
                    .with(new MaterialComponent(material))
                    .with(new NameComponent(name))
                    .with(new RarityComponent(rarity, false)) // Assuming 'false' for glowing by default
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


            if ("HYPERION".equalsIgnoreCase(id)) {
                container = container.with(new ArtOfPeaceComponent(true));
                container = container.with(new HotPotatoBookComponent(100000));
            }

            return new SkyblockItem(id, container);
        }
    }
}