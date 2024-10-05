package io.github.unjoinable.item.component.components;

import io.github.unjoinable.item.SkyblockItem;
import io.github.unjoinable.item.component.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public record MaterialComponent(Material material) implements ItemComponent {
    private static final MaterialComponent DEFAULT = new MaterialComponent(Material.AIR);

    @Override
    public @NotNull Class<? extends ItemComponent> type() {
        return MaterialComponent.class;
    }

    @Override
    public void apply(@NotNull SkyblockItem item, ItemStack.@NotNull Builder builder) {
        builder.material(material);
    }

    @Override
    public @NotNull ItemComponent defaultComponent() {
        return DEFAULT;
    }
}
