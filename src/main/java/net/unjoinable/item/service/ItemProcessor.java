package net.unjoinable.item.service;

import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.unjoinable.item.ItemMetadata;
import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.attribute.AttributeContainer;
import net.unjoinable.item.attribute.traits.ItemAttribute;
import net.unjoinable.item.attribute.traits.NbtAttribute;
import net.unjoinable.registry.Registry;
import net.unjoinable.utility.NamespaceId;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;

/**
 * Processes Skyblock items and converts them between Skyblock and Minestom representations.
 * <p></p>
 * This service handles bidirectional transformation between custom {@link SkyblockItem} objects
 * and Minestom-compatible {@link ItemStack} objects for in-game use.
 */
public class ItemProcessor {
    // Constants
    private static final Tag<String> ID_TAG = Tag.String("id").defaultValue("skyblock:air");

    // Instance fields
    private final Map<Class<? extends NbtAttribute>, Tag<BinaryTag>> cachedTags;
    private final Registry<Class<? extends ItemAttribute>, Codec<? extends ItemAttribute>> attributeCodecRegistry;
    private final Registry<NamespaceId, SkyblockItem> itemRegistry;

    /**
     * Creates a new ItemProcessor with required registries.
     *
     * @param attributeCodecRegistry Registry of codecs for serializing/deserializing attributes
     * @param itemRegistry Registry of all available Skyblock items
     */
    public ItemProcessor(
            @NotNull Registry<Class<? extends ItemAttribute>, Codec<? extends ItemAttribute>> attributeCodecRegistry,
            @NotNull Registry<NamespaceId, SkyblockItem> itemRegistry) {
        this.attributeCodecRegistry = attributeCodecRegistry;
        this.itemRegistry = itemRegistry;
        this.cachedTags = new HashMap<>();
    }

    /**
     * Converts a SkyblockItem into a Minestom ItemStack for in-game use.
     *
     * @param skyblockItem The SkyblockItem to convert
     * @return A fully constructed Minestom ItemStack
     */
    public @NotNull ItemStack toItemStack(@NotNull SkyblockItem skyblockItem) {
        ItemMetadata metadata = skyblockItem.metadata();
        AttributeContainer attributes = skyblockItem.attributes();
        ItemStack.Builder builder = ItemStack.builder(metadata.material());

        applyNbtAttributes(attributes, builder);
        applyDisplayProperties(builder, metadata);
        applyIdTag(builder, metadata.id());

        LoreGenerator loreGenerator = new LoreGenerator(attributes, metadata);
        builder.lore(loreGenerator.generate());

        return builder.build();
    }

    /**
     * Applies all NBT attributes to the ItemStack builder.
     */
    private void applyNbtAttributes(AttributeContainer attributes, ItemStack.Builder builder) {
        for (ItemAttribute attribute : attributes) {
            if (!(attribute instanceof NbtAttribute nbtAttribute)) {
                continue;
            }
            nbtAttribute.asObject().ifPresent(binaryTag ->
                    builder.setTag(getOrCreateTag(nbtAttribute), binaryTag)
            );
        }
    }

    /**
     * Applies display properties to the ItemStack.
     */
    private void applyDisplayProperties(ItemStack.Builder builder, ItemMetadata metadata) {
        builder.customName(text(
                metadata.displayName(),
                metadata.rarity().color()
        ));
    }

    /**
     * Converts a Minestom ItemStack back into a SkyblockItem.
     *
     * @param itemStack The Minestom ItemStack to convert
     * @return A SkyblockItem representing the ItemStack, or AIR if conversion fails
     */
    public @NotNull SkyblockItem fromItemStack(@NotNull ItemStack itemStack) {
        NamespaceId id = retrieveIdTag(itemStack);
        SkyblockItem templateItem = itemRegistry.get(id).orElse(SkyblockItem.AIR);
        AttributeContainer attributes = extractAttributesFromItemStack(itemStack);

        return new SkyblockItem(templateItem.metadata(), attributes);
    }

    /**
     * Extracts attribute data from an ItemStack's NBT tags.
     *
     * @param itemStack The ItemStack to extract attributes from
     * @return A container with all successfully decoded attributes
     */
    private AttributeContainer extractAttributesFromItemStack(@NotNull ItemStack itemStack) {
        AttributeContainer.Builder builder = AttributeContainer.builder();

        cachedTags.forEach((attributeClass, tag) -> attributeCodecRegistry.get(attributeClass).ifPresent(codec -> {
            if (!itemStack.hasTag(tag)) {
                return;
            }

            BinaryTag binaryTag = itemStack.getTag(tag);
            Result<?> result = codec.decode(Transcoder.NBT, binaryTag);

            if (result instanceof Result.Ok(ItemAttribute value)) {
                builder.with(value);
            }
        }));

        return builder.build();
    }

    /**
     * Gets or creates an NBT tag for the given attribute.
     *
     * @param attribute The NBT attribute
     * @return The Tag object for the attribute
     */
    private @NotNull Tag<BinaryTag> getOrCreateTag(@NotNull NbtAttribute attribute) {
        return this.cachedTags.computeIfAbsent(
                attribute.getClass(),
                _ -> Tag.NBT(attribute.id().toString())
        );
    }

    /**
     * Applies the unique identifier tag to the ItemStack.
     *
     * @param builder The ItemStack builder
     * @param id The unique identifier to apply
     */
    private void applyIdTag(@NotNull ItemStack.Builder builder, @NotNull NamespaceId id) {
        builder.setTag(ID_TAG, id.toString());
    }

    /**
     * Retrieves the unique identifier from an ItemStack.
     *
     * @param itemStack The ItemStack to get the ID from
     * @return The unique identifier of the item
     */
    private @NotNull NamespaceId retrieveIdTag(@NotNull ItemStack itemStack) {
        return NamespaceId.fromString(itemStack.getTag(ID_TAG));
    }
}