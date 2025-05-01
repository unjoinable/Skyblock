package net.skyblock.item.component.handlers.trait;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.ItemComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface NBTHandler <C extends ItemComponent> extends ItemComponentHandler<C> {
    /**
     * Deserializes an ItemComponent from NBT data.
     *
     * @param nbt The NBT data containing component information
     * @return An optional new component instance created from the NBT data
     */
    @NotNull Optional<C> fromNbt(CompoundBinaryTag nbt);

    /**
     * Serializes an ItemComponent to NBT data.
     *
     * @param component The component to serialize
     * @return The NBT representation of the component
     */
    CompoundBinaryTag toNbt(@NotNull C component);
}