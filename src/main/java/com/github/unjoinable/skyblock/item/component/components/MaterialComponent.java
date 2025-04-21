package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

/**
 * A final class representing the material component of an item.
 * This class implements the SerializableComponent interface to handle
 * material data for item stack building.
 */
public final class MaterialComponent implements SerializableComponent {
    private final Material material;

    /**
     * Constructs a MaterialComponent with the specified material.
     * @param material The {@link Material} of the item.
     */
    public MaterialComponent(@NotNull Material material) {
        this.material = material;
    }

    /**
     * Retrieves the material associated with this component.
     * @return The {@link Material} of the item.
     */
    public @NotNull Material getMaterial() {
        return material;
    }

    /**
     * Writes the material data to the NBT of an item stack.
     */
    @Override
    public void nbtWriter(ItemStack.@NotNull Builder builder) {
        builder.material(material);
    }
}
