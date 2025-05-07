package net.skyblock.registry.impl;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.handler.*;
import net.skyblock.item.provider.HandlerProvider;
import net.skyblock.registry.base.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A registry for {@link ItemComponentHandler} instances.
 * Provides bidirectional mapping between:
 * - Component classes and their handlers
 * - Component IDs and their handlers
 */
public class HandlerRegistry extends Registry<String, ItemComponentHandler<?>> implements HandlerProvider {
    private final Map<Class<? extends ItemComponent>, ItemComponentHandler<?>> handlersByType = new HashMap<>();

    /**
     * Register a component handler.
     * Uses the handler's componentId() as the key.
     *
     * @param handler The handler to register
     * @throws IllegalArgumentException if a handler is already registered with the same ID
     */
    public void register(@NotNull ItemComponentHandler<?> handler) {
        register(handler.componentId(), handler);
        handlersByType.put(handler.componentType(), handler);
    }

    /**
     * Initialize the registry using reflection to find and register all handlers.
     */
    @Override
    public void init() {
        register(new ArmorColorHandler());
        register(new DescriptionHandler());
        register(new HeadTextureHandler());
        register(new HotPotatoBookHandler());
        register(new ItemCategoryHandler());
        register(new MaterialHandler());
        register(new NameHandler());
        register(new RarityHandler());
        register(new ReforgeHandler());
        register(new StatsHandler(this));
        lock();
    }

    /**
     * Gets the handler for the given component type.
     *
     * @param componentType The class of the component for which to get a handler
     * @param <T> The type of the component handler
     * @return An Optional containing the handler if found, or empty if no matching handler exists
     */
    @Override @SuppressWarnings("unchecked")
    public <T extends ItemComponentHandler<?>> Optional<T> getHandler(Class<? extends ItemComponent> componentType) {
        return Optional.ofNullable((T) handlersByType.get(componentType));
    }

    /**
     * Gets the handler for the given component ID.
     * <p>
     * This method retrieves a handler based on the component ID string.
     *
     * @param componentId The string identifier of the component
     * @return An Optional containing the handler if found, or empty if no matching handler exists
     */
    @Override @SuppressWarnings("unchecked")
    public <T extends ItemComponentHandler<?>> Optional<T> getHandler(String componentId) {
        return Optional.ofNullable((T) get(componentId));
    }

    /**
     * Retrieves all registered component handlers.
     * <p>
     * This method provides access to the complete collection of all available handlers.
     *
     * @return A collection containing all registered handlers
     */
    @Override
    public Collection<ItemComponentHandler<?>> getAllHandlers() {
        return handlersByType.values();
    }

    /**
     * Called when registry is locked to prevent further modifications.
     * Ensures the type map is consistent with the registry.
     */
    @Override
    public void lock() {
        super.lock();
        handlersByType.clear();
        values().forEach(handler -> handlersByType.put(handler.componentType(), handler));
    }
}