package net.skyblock.item.component.definition;

import net.minestom.server.item.Material;
import net.skyblock.item.component.ItemComponent;

/**
 * Component defining the base Minecraft material for an item.
 * <p>
 * This component determines the fundamental appearance and behavior of the item
 * before any custom modifications are applied.
 */
public record MaterialComponent(Material material) implements ItemComponent {

    /**
     * Creates a new MaterialComponent with the specified material.
     *
     * @param material The Minecraft material to use for this item
     * @return A new MaterialComponent with the updated material
     */
    public MaterialComponent with(Material material) {
        return new MaterialComponent(material);
    }
}