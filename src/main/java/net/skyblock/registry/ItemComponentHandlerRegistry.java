package net.skyblock.registry;

import net.skyblock.item.ItemComponentHandler;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.handlers.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A registry for {@link ItemComponentHandler} instances.
 * Provides bidirectional mapping between:
 * - Component classes and their handlers
 * - Component IDs and their handlers
 */
public class ItemComponentHandlerRegistry extends Registry<String, ItemComponentHandler<?>> {

    /**
     * Register a component handler.
     * Uses the handler's componentId() as the key.
     *
     * @param handler The handler to register
     * @throws IllegalArgumentException if a handler is already registered with the same ID
     */
    public void register(@NotNull ItemComponentHandler<?> handler) {
        register(handler.componentId(), handler);
    }

    /**
     * Get a component handler by component class.
     *
     * @param componentClass The component class to look up
     * @param <C>            The component type
     * @return The handler for the given component type, or null if not found
     */
    @SuppressWarnings("unchecked")
    public @Nullable <C extends ItemComponent> ItemComponentHandler<C> getHandler(@NotNull Class<C> componentClass) {
        return (ItemComponentHandler<C>) find(handler ->
                handler.componentType().equals(componentClass)
        ).orElse(null);
    }

    /**
     * Get a component handler by component ID.
     *
     * @param componentId The component ID to look up
     * @return The handler for the given component ID, or null if not found
     */
    public @Nullable ItemComponentHandler<?> getHandler(@NotNull String componentId) {
        return get(componentId);
    }

    /**
     * Check if a handler exists for the given component class.
     *
     * @param componentClass The component class to check
     * @return true if a handler is registered for this component class
     */
    public boolean hasHandler(@NotNull Class<? extends ItemComponent> componentClass) {
        return find(handler -> handler.componentType().equals(componentClass)).isPresent();
    }

    /**
     * Check if a handler exists for the given component ID.
     *
     * @param componentId The component ID to check
     * @return true if a handler is registered with this ID
     */
    public boolean hasHandler(@NotNull String componentId) {
        return containsKey(componentId);
    }

    /**
     * Initialize the registry with default handlers.
     * Override this method to register default handlers.
     */
    @Override
    public void init() {
        register(new RarityHandler());
        register(new NameHandler());
        register(new HeadTextureHandler());
        register(new HotPotatoBookHandler());
        register(new MaterialHandler());
        register(new StatsHandler());
    }
}