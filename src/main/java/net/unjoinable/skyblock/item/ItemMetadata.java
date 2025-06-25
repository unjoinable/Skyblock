package net.unjoinable.skyblock.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import net.minestom.server.item.Material;
import net.unjoinable.skyblock.item.enums.ItemCategory;
import net.unjoinable.skyblock.item.enums.Rarity;
import net.unjoinable.skyblock.utility.NamespaceId;

import java.util.List;

/**
 * Represents static metadata associated with a Skyblock-style item.
 * <p>
 * This class is a plain data container that holds core identifying and
 * descriptive attributes of an item, separate from its behavior or logic.
 * It is designed to be immutable and safely shareable between item instances.
 *
 * <p><b>Note:</b> Behavioral components or dynamic logic should not be stored
 * in this class. Use {@code SkyblockItem} or a similar class to associate this
 * metadata with a component system.
 *
 * @param id          Unique identifier for the item, typically in namespaced format (e.g. {@code skyblock:aspect_of_the_end}).
 * @param displayName The name used to render in item's name
 * @param material    The Minecraft {@link Material} used for rendering the item in inventory and hand.
 * @param category    The {@link ItemCategory} of the item (e.g., WEAPON, TOOL, MISC).
 * @param rarity      The {@link Rarity}, which may determine color coding, drop rates, or progression level.
 * @param description A list of rich text {@link Component}s that represent the item's lore or detailed description.
 */
public record ItemMetadata(
        NamespaceId id,
        String displayName,
        Material material,
        ItemCategory category,
        Rarity rarity,
        List<Component> description) {

    public static final Codec<ItemMetadata> CODEC = StructCodec.struct(
            "id", NamespaceId.CODEC, ItemMetadata::id,
            "name", Codec.STRING, ItemMetadata::displayName,
            "material", Material.CODEC, ItemMetadata::material,
            "category", Codec.Enum(ItemCategory.class), ItemMetadata::category,
            "rarity", Codec.Enum(Rarity.class), ItemMetadata::rarity,
            "description", Codec.COMPONENT.list(), ItemMetadata::description,
            ItemMetadata::new
    );

    public static final ItemMetadata DEFAULT = new ItemMetadata(
            NamespaceId.fromSkyblock("air"),
            "",
            Material.AIR,
            ItemCategory.NONE,
            Rarity.UNOBTAINABLE,
            List.of());
}
