package net.skyblock.item.io;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.tag.Tag;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.trait.NBTHandler;
import net.skyblock.item.component.trait.StackWriterHandler;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.provider.HandlerProvider;
import net.skyblock.item.provider.ItemProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

/**
 * Processor for converting between Skyblock items and Minestom ItemStacks
 */
public class SkyblockItemProcessor implements ItemProcessor {
    private static final Tag<String> ID_TAG = Tag.String("skyblock_id");
    private final HandlerProvider handlerProvider;
    private final ItemProvider itemProvider;

    public SkyblockItemProcessor(@NotNull HandlerProvider handlerProvider, @NotNull ItemProvider itemProvider) {
        this.handlerProvider = handlerProvider;
        this.itemProvider = itemProvider;
    }

    /**
     * Converts a SkyblockItem to an ItemStack
     */
    public @NotNull ItemStack toItemStack(@NotNull SkyblockItem skyblockItem) {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR);

        // Process components
        skyblockItem.components().asMap()
                .values().forEach(component -> processComponent(builder, component, skyblockItem.components()));

        builder.lore(new LoreGenerator(skyblockItem, handlerProvider).generate());
        builder.setTag(ID_TAG, skyblockItem.itemId());
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, new AttributeList(Collections.emptyList()));

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private <T extends ItemComponent> void processComponent(ItemStack.Builder builder, T component, ComponentContainer container) {
        if (component == null) return;

        try {
            ItemComponentHandler<T> handler = (ItemComponentHandler<T>) handlerProvider.getHandler(component.getClass()).get();

            // Handle NBT data
            if (handler instanceof NBTHandler<T> nbtHandler) {
                CompoundBinaryTag tag = nbtHandler.toNbt(component);
                if (tag != null) {
                    builder.set(Tag.NBT(nbtHandler.componentId()), tag);
                }
            }

            // Handle Stack Writers
            if (handler instanceof StackWriterHandler<T> stackWriter) {
                stackWriter.write(component, builder, container);
            }
        } catch (Exception _) {} //ignored
    }

    /**
     * Converts an ItemStack to a SkyblockItem
     */
    public @NotNull SkyblockItem toSkyblockItem(@NotNull ItemStack itemStack) {
        if (!itemStack.hasTag(ID_TAG)) {
            return SkyblockItem.AIR;
        }

        // Get the base item from registry
        String itemId = itemStack.getTag(ID_TAG);
        SkyblockItem baseItem = itemProvider.getItem(itemId);

        // Deserialize components from NBT
        ComponentContainer container = baseItem.components();

        // Process each NBT handler
        for (ItemComponentHandler<?> handler : handlerProvider.getAllHandlers()) {
            if (handler instanceof NBTHandler<?> nbtHandler) {
                try {
                    CompoundBinaryTag nbtData = (CompoundBinaryTag) itemStack.getTag(Tag.NBT(handler.componentId()));
                    Optional<?> componentOpt = nbtHandler.fromNbt(nbtData);

                    if (componentOpt.isPresent()) {
                        container = container.with((ItemComponent) componentOpt.get());
                    }
                } catch (Exception _) {} //ignored
            }
        }

        return new SkyblockItem(itemId, container);
    }
}