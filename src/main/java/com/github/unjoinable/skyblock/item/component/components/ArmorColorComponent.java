package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.component.BasicComponent;
import com.github.unjoinable.skyblock.util.Utils;
import net.minestom.server.color.Color;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;
import org.jetbrains.annotations.NotNull;

public record ArmorColorComponent(int[] color) implements BasicComponent {

    @Override
    public void applyEffect(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        Material material = item.container().getComponent(MaterialComponent.class).material();
        if (Utils.isLeather(material)) {
            if (color.length > 3) throw new IllegalStateException("Invalid RGB Color format");
            Color color = new Color(color()[0], color()[1], color()[2]);
            builder.set(net.minestom.server.item.ItemComponent.DYED_COLOR, new DyedItemColor(color));
        }
    }
}
