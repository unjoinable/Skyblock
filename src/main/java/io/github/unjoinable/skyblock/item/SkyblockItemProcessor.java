package io.github.unjoinable.skyblock.item;

import io.github.unjoinable.skyblock.item.component.BasicComponent;
import io.github.unjoinable.skyblock.item.component.Component;
import io.github.unjoinable.skyblock.registry.registries.ItemRegistry;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;

import java.util.List;
import java.util.Map;

public class SkyblockItemProcessor {
    private static final ItemRegistry itemRegistry = ItemRegistry.getInstance();
    private static final Tag<String> ID_TAG = Tag.String("id");

    public SkyblockItem getItem(String id) {
        id = id.toUpperCase();
        if (itemRegistry.contains(id)) {
            return itemRegistry.get(id);
        }
        return SkyblockItem.AIR;
    }

    public ItemStack toItemStack(SkyblockItem item) {
        ItemStack.Builder builder = ItemStack.builder(Material.AIR);
        Map<Class<? extends Component>, Component> components = item.container().getAllComponents();

        for (Component component : components.values()) {
            if (component instanceof BasicComponent) {
                ((BasicComponent) component).applyEffect(item, builder);
            }
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
