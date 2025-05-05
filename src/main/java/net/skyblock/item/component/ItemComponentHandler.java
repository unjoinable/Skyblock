package net.skyblock.item.component;

import com.google.gson.JsonElement;
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

    /**
     * Creates a component instance from JSON data
     *
     * @param json The JSON data to parse
     * @return The created component instance
     * @throws UnsupportedOperationException by default unless overridden
     */
    default @NotNull C fromJson(@NotNull JsonElement json) {
        throw new UnsupportedOperationException("fromJson not implemented for component: " + componentId());
    }
}