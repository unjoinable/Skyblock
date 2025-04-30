package net.skyblock.item;

import net.skyblock.item.component.ItemComponent;
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

//    /**
//     * Creates a new component instance with default parameters.
//     * <p>
//     * This factory method provides a convenient way to create
//     * components with sensible default values.
//     *
//     * @return A new component instance with default parameters
//     */
//    @NotNull C createDefault();
}