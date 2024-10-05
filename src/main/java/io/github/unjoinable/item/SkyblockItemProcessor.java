package io.github.unjoinable.item;

import com.google.common.collect.ImmutableSet;
import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.item.component.Retrievable;
import io.github.unjoinable.item.component.components.UUIDComponent;
import io.github.unjoinable.registry.registries.ItemRegistry;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkyblockItemProcessor {
    private static final ItemRegistry itemRegistry = ItemRegistry.getInstance();
    private static final Tag<String> ID_TAG = Tag.String("id");
    private static final Set<Class<? extends Retrievable>> RETRIEABLE_COMPONENTS = ImmutableSet.of(UUIDComponent.class);

    public SkyblockItem getItem(String id) {
        id = id.toUpperCase();
        if (itemRegistry.contains(id)) {
            return itemRegistry.get(id);
        }
        return SkyblockItem.AIR;
    }

    public ItemStack toItemStack(SkyblockItem item) {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR);
        Map<Class<? extends ItemComponent>, ItemComponent> components = item.components();

        for (ItemComponent component : components.values()) {
            component.apply(item, builder);
        }

        builder.set(ID_TAG, item.id());
        builder.set(net.minestom.server.item.ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        builder.set(net.minestom.server.item.ItemComponent.ATTRIBUTE_MODIFIERS, new AttributeList(List.of(), false));
        builder.lore(new LoreSystem(item, null).build());
        return builder.build();
    }

    public SkyblockItem fromItemStack(ItemStack item) {
        if (!item.hasTag(ID_TAG)) return SkyblockItem.AIR;
        SkyblockItem skyblockItem = this.getItem(item.getTag(ID_TAG));
        return skyblockItem;
    }
}
