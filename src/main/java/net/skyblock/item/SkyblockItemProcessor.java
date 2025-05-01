package net.skyblock.item;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.tag.Tag;
import net.skyblock.Skyblock;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.handlers.trait.NBTHandler;
import net.skyblock.item.component.handlers.trait.StackWriterHandler;
import net.skyblock.registry.HandlerRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Optional;

/**
 * A processor for converting between Skyblock items and Minestom ItemStacks.
 * <p>
 * This class handles serialization of SkyblockItem to ItemStack and
 * deserialization of ItemStack back to SkyblockItem, preserving all components.
 * It ensures all NBT data and component properties are correctly transferred
 * during the conversion process.
 */
public class SkyblockItemProcessor {
    private static final Tag<String> ID_TAG = Tag.String("skyblock_id").defaultValue("AIR");

    private final Logger logger;
    private final HandlerRegistry registry;

    /**
     * Creates a new SkyblockItemProcessor with the specified component registry.
     *
     * @param registry The registry containing all component handlers for serialization/deserialization
     * @throws IllegalArgumentException if registry is null
     */
    public SkyblockItemProcessor(@NotNull HandlerRegistry registry) {
        this.registry = registry;
        this.logger = Skyblock.getLogger();
    }

    /**
     * Converts a SkyblockItem to an ItemStack.
     * <p>
     * This method creates a Minestom ItemStack that represents the provided SkyblockItem,
     * preserving all component data through NBT.
     *
     * @param skyblockItem The SkyblockItem to convert
     * @return An ItemStack representing the SkyblockItem
     * @throws IllegalArgumentException if skyblockItem is null
     */
    public @NotNull ItemStack toItemStack(@NotNull SkyblockItem skyblockItem) {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR);

        // Process all components in the item
        skyblockItem.components().asMap().values().forEach(component ->
                processComponent(builder, component)
        );

        // Set common properties
        builder.lore(new LoreGenerator(skyblockItem).generate());
        builder.setTag(ID_TAG, skyblockItem.itemId());
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, new AttributeList(Collections.emptyList()));

        return builder.build();
    }

    /**
     * Processes a single component and adds its data to the ItemStack builder.
     *
     * @param builder The ItemStack builder to modify
     * @param component The component to process
     */
    @SuppressWarnings("unchecked")
    private <T extends ItemComponent> void processComponent(ItemStack.Builder builder, T component) {
        if (component == null) return;

        try {
            ItemComponentHandler<T> handler = (ItemComponentHandler<T>) registry.getHandler(component.getClass());

            // Handle NBT data if applicable
            if (handler instanceof NBTHandler<T> nbtHandler) {
                CompoundBinaryTag tag = nbtHandler.toNbt(component);
                if (tag != null) {
                    builder.set(Tag.NBT(nbtHandler.componentId()), tag);
                }
            }

            // Apply direct ItemStack modifications if applicable
            if (handler instanceof StackWriterHandler<T> stackWriterHandler) {
                stackWriterHandler.write(component, builder);
            }
        } catch (Exception e) {
            logger.warn("Failed to process component: {}", component.getClass().getName(), e);
        }
    }

    /**
     * Converts an ItemStack to a SkyblockItem by deserializing its components.
     * <p>
     * This method extracts the Skyblock item ID and all component data from the ItemStack
     * and reconstructs the original SkyblockItem.
     *
     * @param itemStack The ItemStack to convert
     * @return A SkyblockItem containing all deserialized components
     * @throws IllegalArgumentException if itemStack is null
     */
    public @NotNull SkyblockItem toSkyblockItem(@NotNull ItemStack itemStack) {
        // Return default if no skyblock ID is present
        if (!itemStack.hasTag(ID_TAG)) {
            return SkyblockItem.AIR;
        }

        // Get the base item from registry
        String itemId = itemStack.getTag(ID_TAG);
        SkyblockItem baseItem = Skyblock.getInstance().getItemRegistry().get(itemId);
        if (baseItem == null) {
            logger.warn("Unknown Skyblock item ID: {}", itemId);
            return SkyblockItem.AIR;
        }

        // Start with base components and deserialize from NBT
        ComponentContainer container = baseItem.components();
        container = deserializeComponentsFromNBT(itemStack, container);

        return new SkyblockItem(itemId, container);
    }

    /**
     * Deserializes all components from the ItemStack's NBT data.
     *
     * @param itemStack The ItemStack containing the NBT data
     * @param container The initial component container
     * @return The updated component container with deserialized components
     */
    private ComponentContainer deserializeComponentsFromNBT(
            @NotNull ItemStack itemStack,
            @NotNull ComponentContainer container) {

        ComponentContainer updatedContainer = container;

        // Process each registered NBT handler
        for (ItemComponentHandler<?> handler : registry.values()) {
            if (handler instanceof NBTHandler<?> nbtHandler) {
                updatedContainer = deserializeComponentFromNBT(itemStack, updatedContainer, nbtHandler);
            }
        }

        return updatedContainer;
    }

    /**
     * Deserializes a single component from NBT data.
     *
     * @param itemStack The ItemStack containing the NBT data
     * @param container The current component container
     * @param handler The NBT handler for the component
     * @return The updated component container
     */
    private ComponentContainer deserializeComponentFromNBT(
            @NotNull ItemStack itemStack,
            @NotNull ComponentContainer container,
            @NotNull NBTHandler<?> handler) {

        try {
            String componentId = handler.componentId();
            CompoundBinaryTag nbtData = itemStack.toItemNBT().getCompound(componentId);

            Optional<?> componentOpt = handler.fromNbt(nbtData);
            if (componentOpt.isPresent()) {
                ItemComponent component = (ItemComponent) componentOpt.get();
                return container.with(component);
            }
        } catch (Exception e) {
            logger.warn("Failed to deserialize component: {}", handler.componentId(), e);
        }

        return container;
    }
}