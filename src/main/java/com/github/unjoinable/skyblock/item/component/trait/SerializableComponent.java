package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for components that can be serialized to persistent storage.
 */
public interface SerializableComponent extends Component {
    /**
     * Applies this component's data to an ItemStack builder
     */
    void nbtWriter(ItemStack.@NotNull Builder builder);
}
