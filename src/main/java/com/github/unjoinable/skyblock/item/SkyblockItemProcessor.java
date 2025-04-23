package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.Component;
import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.trait.DeserializableComponent;
import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import com.github.unjoinable.skyblock.registry.Registry;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A processor for converting between Skyblock items and Minestom ItemStacks.
 * <p>
 * This class handles serialization of SkyblockItem to ItemStack and
 * deserialization of ItemStack back to SkyblockItem, preserving all components.
 */
public class SkyblockItemProcessor {
    private static final Tag<String> ID_TAG = Tag.String("id").defaultValue("AIR");
    private final List<Function<ItemStack, Optional<? extends DeserializableComponent>>> deserializers = new ArrayList<>();

    /**
     * Registers a component deserializer function.
     *
     * @param deserializer Function that attempts to create a component from an ItemStack
     */
    public void registerDeserializer(Function<ItemStack, Optional<? extends DeserializableComponent>> deserializer) {
        deserializers.add(deserializer);
    }

    /**
     * Converts a SkyblockItem to an ItemStack.
     *
     * @param skyblockItem The SkyblockItem to convert
     * @return An ItemStack representing the SkyblockItem
     */
    public @NotNull ItemStack toItemStack(@NotNull SkyblockItem skyblockItem) {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR); // Material by component

        for (Component component : skyblockItem.components().asMap().values()) {
            if (component instanceof SerializableComponent serializableComponent) {
                serializableComponent.write(builder);
            }
        }

        builder.lore(new LoreGenerator(skyblockItem).generate());
        builder.setTag(ID_TAG, skyblockItem.itemId());
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, new AttributeList(List.of()));
        return builder.build();
    }

    /**
     * Converts an ItemStack to a SkyblockItem by deserializing its components.
     *
     * @param itemStack The ItemStack to convert
     * @return A SkyblockItem containing all deserialized components
     */
    public @NotNull SkyblockItem toSkyblockItem(@NotNull ItemStack itemStack) {
        SkyblockItem item = Registry.ITEM_REGISTRY.get(itemStack.getTag(ID_TAG));
        if (item == null) return SkyblockItem.AIR;

        ComponentContainer container = item.components();

        for (var deserializer : deserializers) {
            Optional<? extends DeserializableComponent> componentOpt = deserializer.apply(itemStack);
            if (componentOpt.isPresent()) {
                container = container.with(componentOpt.get());
            }
        }

        return new SkyblockItem(item.itemId(), container);
    }
}