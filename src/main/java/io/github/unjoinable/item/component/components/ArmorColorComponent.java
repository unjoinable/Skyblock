package io.github.unjoinable.item.component.components;

import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import io.github.unjoinable.util.Utils;
import net.minestom.server.color.Color;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import org.jetbrains.annotations.NotNull;

public record ArmorColorComponent(int[] color) implements ItemComponent {
    private static final ArmorColorComponent DEFAULT = new ArmorColorComponent(null);


    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return ArmorColorComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        Material material = item.getOrDefault(MaterialComponent.class).material();
        if (Utils.isLeather(material)) {
            if (color.length > 3) throw new IllegalStateException("Invalid RGB Color format");
            Color color = new Color(color()[0], color()[1], color()[2]);
            builder.set(net.minestom.server.item.ItemComponent.DYED_COLOR, new DyedItemColor(color));
        }
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }
}
