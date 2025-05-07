package net.skyblock.item.provider;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;

import java.util.Collection;
import java.util.Optional;

/**
 * A service interface that provides access to item component handlers.
 * This interface decouples components from the concrete {@code HandlerRegistry}
 * implementation, allowing for more flexible and testable code.
 * <p>
 * The HandlerProvider acts as a facade for handler discovery and retrieval,
 * abstracting away the details of how handlers are stored and registered.
 * <p>
 * Each component type has exactly one handler of a specific handler type.
 */
public interface HandlerProvider {

    /**
     * Gets the handler for the given component type and handler type.
     * <p>
     * This method returns an Optional that may contain the requested handler if one
     * exists for the specified component type and handler interface.
     *
     * @param <T> The type of handler to retrieve
     * @param componentType The class of the component for which to get a handler
     * @return An Optional containing the handler if found, or empty if no matching handler exists
     */
    <T extends ItemComponentHandler<?>> Optional<T> getHandler(Class<? extends ItemComponent> componentType);

    /**
     * Gets the handler for the given component instance and handler type.
     * <p>
     * This is a convenience method that uses the runtime type of the component.
     *
     * @param <T> The type of handler to retrieve
     * @param component The component for which to get a handler
     * @return An Optional containing the handler if found, or empty if no matching handler exists
     */
    default <T extends ItemComponentHandler<?>> Optional<T> getHandler(ItemComponent component) {
        return getHandler(component.getClass());
    }

    /**
     * Gets the handler for the given component ID.
     * <p>
     * This method retrieves a handler based on the component ID string.
     *
     * @param <T> The type of handler to retrieve
     * @param componentId The string identifier of the component
     * @return An Optional containing the handler if found, or empty if no matching handler exists
     */
    <T extends ItemComponentHandler<?>> Optional<T> getHandler(String componentId);

    /**
     * Checks if a handler exists for the specified component type and handler interface.
     *
     * @param componentType The class of the component
     * @return true if a matching handler exists, false otherwise
     */
    default boolean hasHandler(Class<? extends ItemComponent> componentType) {
        return getHandler(componentType).isPresent();
    }

    /**
     * Checks if a handler exists for the specified component ID.
     *
     * @param componentId The string identifier of the component
     * @return true if a matching handler exists, false otherwise
     */
    default boolean hasHandler(String componentId) {
        return getHandler(componentId).isPresent();
    }

    /**
     * Retrieves all registered component handlers.
     * <p>
     * This method provides access to the complete collection of all available handlers.
     *
     * @return A collection containing all registered handlers
     */
    Collection<ItemComponentHandler<?>> getAllHandlers();
}