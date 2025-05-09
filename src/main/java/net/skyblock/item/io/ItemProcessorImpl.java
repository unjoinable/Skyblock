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

    public ItemProcessorImpl(@NotNull CodecProvider codecProvider, @NotNull ItemProvider itemProvider) {
        this.codecProvider = codecProvider;
        this.itemProvider = itemProvider;
    }

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
     * Processes a codec attribute and applies it to the item stack builder
     *
     * @param builder      The item stack builder
     * @param nbtAttribute The nbt attribute to process
     */
    @SuppressWarnings("unchecked")
    private void processNbtAttribute(ItemStack.Builder builder, NbtAttribute nbtAttribute) {
        Codec<NbtAttribute> codec = (Codec<NbtAttribute>) nbtAttribute.getCodec();
        codecProvider.getTag(nbtAttribute.id())
                .ifPresent(tag -> builder.set(tag, codec.encode(Transcoder.NBT, nbtAttribute).orElseThrow()));
    }

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