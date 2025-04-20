package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.trait.NBTWritable;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

/**
 * A final class representing the material component of an item.
 * This class implements the NBTWritable interface to handle
 * material data for item stack building.
 */
public final class MaterialComponent implements NBTWritable {
    private final Material material;

    /**
     * Constructs a MaterialComponent with the specified material.
     *
     * @param material The {@link Material} of the item.
     */
    public MaterialComponent(Material material) {
        this.material = material;
    }

    /**
     * Retrieves the material associated with this component.
     *
     * @return The {@link Material} of the item.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Writes the material data to the NBT of an item stack.
     *
     * @return A {@link UnaryOperator} that modifies the {@link ItemStack.Builder}.
     */
    @Override
    public @NotNull UnaryOperator<ItemStack.Builder> nbtWriter() {
        return builder -> builder.material(material);
    }
}