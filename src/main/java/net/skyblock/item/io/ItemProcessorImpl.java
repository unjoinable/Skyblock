package net.skyblock.item.io;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.Result;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.NbtAttribute;
import net.skyblock.item.attribute.base.StackAttribute;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.provider.CodecProvider;
import net.skyblock.item.provider.ItemProvider;
import org.jetbrains.annotations.NotNull;

public class ItemProcessorImpl implements ItemProcessor {
    private static final Tag<String> ID_TAG = Tag.String("skyblock_id");
    private final ItemProvider itemProvider;
    private final CodecProvider codecProvider;

    /**
     * Constructs an ItemProcessorImpl with the specified codec and item providers.
     *
     * @param codecProvider provider for attribute codecs and tags
     * @param itemProvider provider for retrieving SkyblockItem instances by ID
     */
    public ItemProcessorImpl(@NotNull CodecProvider codecProvider, @NotNull ItemProvider itemProvider) {
        this.codecProvider = codecProvider;
        this.itemProvider = itemProvider;
    }

    /**
     * Converts a {@link SkyblockItem} into an {@link ItemStack}, encoding its attributes and metadata.
     *
     * Encodes all {@link NbtAttribute}s as NBT tags and applies all {@link StackAttribute}s to the item stack builder.
     * The resulting {@link ItemStack} includes the skyblock item ID as a tag and generated lore.
     *
     * @param skyblockItem the skyblock item to convert
     * @return an {@link ItemStack} representing the given skyblock item
     */
    @Override
    public @NotNull ItemStack toItemStack(@NotNull SkyblockItem skyblockItem) {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR);

        skyblockItem.attributes().stream().forEach(attribute -> {
            if (attribute instanceof NbtAttribute nbtAttribute) {
                processNbtAttribute(builder, nbtAttribute);
            }

            if (attribute instanceof StackAttribute stackAttribute) {
                stackAttribute.applyToBuilder(builder, skyblockItem.attributes());
            }
        });

        builder.setTag(ID_TAG, skyblockItem.itemId());
        builder.lore(new LoreGenerator(skyblockItem).generate());
        return builder.build();
    }

    /**
     * Encodes an NbtAttribute using its codec and sets the resulting tag value on the ItemStack builder.
     *
     * @param builder the ItemStack builder to apply the encoded attribute to
     * @param nbtAttribute the NbtAttribute to encode and apply
     */
    @SuppressWarnings("unchecked")
    private void processNbtAttribute(ItemStack.Builder builder, NbtAttribute nbtAttribute) {
        Codec<NbtAttribute> codec = (Codec<NbtAttribute>) nbtAttribute.getCodec();
        codecProvider.getTag(nbtAttribute.id())
                .ifPresent(tag -> builder.set(tag, codec.encode(Transcoder.NBT, nbtAttribute).orElseThrow()));
    }

    /**
     * Converts an {@link ItemStack} to a {@link SkyblockItem} by decoding its stored tags and reconstructing its attributes.
     *
     * If the item stack does not contain a valid Skyblock item ID tag, returns {@link SkyblockItem#AIR}.
     *
     * @param itemStack the item stack to convert
     * @return the reconstructed Skyblock item
     */
    @Override
    public @NotNull SkyblockItem toSkyblockItem(@NotNull ItemStack itemStack) {
        if (!itemStack.hasTag(ID_TAG)) {
            return SkyblockItem.AIR;
        }

        String itemId = itemStack.getTag(ID_TAG);
        SkyblockItem baseItem = itemProvider.getItem(itemId);
        AttributeContainer.Builder builder = baseItem.attributes().toBuilder();

        codecProvider.getTags().forEach((id, tag) -> {
            if (itemStack.hasTag(tag)) {
                Codec<?> codec = codecProvider.getCodec(id).orElseThrow();
                Result<?> result = codec.decode(Transcoder.NBT, itemStack.getTag(tag));

                if (result instanceof Result.Ok(var attribute)) {
                    builder.with((NbtAttribute) attribute);
                }
            }
        });

        return new SkyblockItem(itemId, builder.build());
    }
}