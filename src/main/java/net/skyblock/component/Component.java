package net.skyblock.component;

import org.jetbrains.annotations.NotNull;

/**
 * Base marker interface for all components that can be stored in a {@link ComponentContainer}.
 */
public interface Component {
    /**
     * Gets the component's type class.
     * Implementations should typically return their own class.
     *
     * @return the class object representing this component's type
     */
    default @NotNull Class<? extends Component> getType() {
        return this.getClass();
    }
}