package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Interface for components that can be deserialized from NBT
 */
public interface NBTReadable extends Component {

    /**
     * Attempts to create a component from an ItemStack's NBT data
     * @return The deserialized component, or empty if invalid/missing data
     */
    @NotNull Optional<? extends NBTReadable> fromNBT(@NotNull ItemStack itemStack);

}
