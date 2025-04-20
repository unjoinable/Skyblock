package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

/**
 * Marker interface for all components that can be serialized to NBT
 */
public interface NBTWritable extends Component {

    /**
     * Returns a function that applies this component's NBT data to an ItemStack builder
     */
    @NotNull UnaryOperator<ItemStack.Builder> nbtWriter();
}
