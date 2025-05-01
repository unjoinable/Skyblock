package net.skyblock.item.component;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for handling specific {@link ItemComponent} types.
 *
 * @param <C> The type of {@link ItemComponent} this handler manages
 */
public interface ItemComponentHandler<C extends ItemComponent> {

    /**
     * Returns the component type this handler is responsible for
     */
    @NotNull Class<C> componentType();

    /**
     * Returns the unique identifier for this component type
     */
    @NotNull String componentId();
}