package net.skyblock.item;

import net.minestom.server.item.Material;
import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.components.*;
import net.skyblock.item.enums.ItemCategory;
import net.skyblock.item.enums.Rarity;
import net.skyblock.stats.StatProfile;

import java.util.List;

/**
 * A builder class for constructing {@link SkyblockItem} instances.
 * This class is typically used during JSON deserialization to populate
 * item properties before building the final immutable {@link SkyblockItem}.
 */
@SuppressWarnings("unused")
public class SkyblockItemBuilder {
    private String id;
    private Material material;
    private String name;
    private Rarity rarity;
    private ItemCategory category;
    private StatProfile statistics;
    private String skin; // Used for head textures
    private String color; // Used for leather armor color

    private ComponentContainer container = new ComponentContainer();
    private List<Component> description;

    /**
     * Creates a new builder instance.
     * Package-private constructor - instances should be created via SkyblockItem.builder()
     * or deserialization processes.
     */
    SkyblockItemBuilder() {}

    /**
     * Sets the item's ID.
     *
     * @param id The unique identifier for this item
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the item's material.
     *
     * @param material The Minecraft material for this item
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the item's display name.
     *
     * @param name The display name for this item
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the item's rarity.
     *
     * @param rarity The rarity level for this item
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    /**
     * Sets the item's category.
     *
     * @param category The category this item belongs to
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder category(ItemCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Sets the item's statistics profile.
     *
     * @param statistics The stats profile for this item
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder statistics(StatProfile statistics) {
        this.statistics = statistics;
        return this;
    }

    /**
     * Sets the item's skin texture (for head items).
     *
     * @param skin The texture string for this item
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder skin(String skin) {
        this.skin = skin;
        return this;
    }

    /**
     * Sets the item's color (for leather armor).
     *
     * @param color The color string for this item
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder color(String color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the item's description.
     *
     * @param description The list of description components
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder description(List<Component> description) {
        this.description = description;
        return this;
    }

    /**
     * Sets a custom component container.
     *
     * @param container The component container to use
     * @return This builder instance for chaining
     */
    public SkyblockItemBuilder container(ComponentContainer container) {
        this.container = container;
        return this;
    }

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

        if (color != null) {
            container = container.with(new ArmorColorComponent(color));
        }

        return new SkyblockItem(id, container);
    }
}