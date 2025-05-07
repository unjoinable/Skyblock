package net.skyblock.item.component;

import net.skyblock.component.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Base marker interface for all components.
 * Implementations must be immutable and thread-safe.
 */
public interface ItemComponent extends Component {
    /**
     * Gets the component's specific type class.
     * This is used as the key in the component container.
     *
     * @return the class object representing this component's type
     */
    default @Override @NotNull Class<? extends ItemComponent> getType() {
        return this.getClass();
    }
}