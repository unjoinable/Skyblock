package net.skyblock.item.component.trait;

import net.skyblock.item.component.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for components that can be serialized to persistent storage.
 */
public interface SerializableComponent extends Component {
    /**
     * Applies this component's data to an ItemStack builder
     */
    void write(ItemStack.@NotNull Builder builder);
}
