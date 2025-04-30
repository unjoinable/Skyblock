package net.skyblock.item.handlers;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.skyblock.item.ItemComponentHandler;
import net.skyblock.item.component.ItemComponent;
import org.jetbrains.annotations.NotNull;

public interface NBTHandler <C extends ItemComponent> extends ItemComponentHandler<C> {
    /**
     * Deserializes an ItemComponent from NBT data.
     *
     * @param nbt The NBT data containing component information
     * @return A new component instance created from the NBT data
     */
    @NotNull C fromNbt(CompoundBinaryTag nbt);

    /**
     * Serializes an ItemComponent to NBT data.
     *
     * @param component The component to serialize
     * @return The NBT representation of the component
     */
    CompoundBinaryTag toNbt(@NotNull C component);
}