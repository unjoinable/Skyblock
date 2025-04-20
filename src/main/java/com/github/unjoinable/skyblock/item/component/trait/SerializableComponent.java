package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

/**
 * Interface for components that can be serialized to persistent storage.
 */
public interface SerializableComponent extends Component {
    /**
     * Returns a function that applies this component's data to an ItemStack builder
     */
    @NotNull UnaryOperator<ItemStack.Builder> nbtWriter();
}
