package net.skyblock.registry;

import net.skyblock.Skyblock;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ItemComponentHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Modifier;

/**
 * A registry for {@link ItemComponentHandler} instances.
 * Provides bidirectional mapping between:
 * - Component classes and their handlers
 * - Component IDs and their handlers
 */
public class HandlerRegistry extends Registry<String, ItemComponentHandler<?>> {
    private static final String HANDLERS_PACKAGE = "net.skyblock.item.component.handlers";

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
     * @return The handler for the given component type, or null if not found
     */
    public @Nullable ItemComponentHandler<?> getHandler(@NotNull Class<? extends ItemComponent> componentClass) {
        return find(handler -> handler.componentType().equals(componentClass)).orElse(null);
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
     * Initialize the registry using reflection to find and register all handlers.
     */
    @Override
    public void init() {
        try {
            Reflections reflections = new Reflections(HANDLERS_PACKAGE, new SubTypesScanner(false));

            int count = 0;
            for (Class<? extends ItemComponentHandler> cls : reflections.getSubTypesOf(ItemComponentHandler.class)) {
                if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers()))
                    continue;

                try {
                    register(cls.getDeclaredConstructor().newInstance());
                    count++;
                } catch (Exception e) {
                    Skyblock.getLogger().warn("Failed to load handler: {}", cls.getName(), e);
                }
            }

            Skyblock.getLogger().info("Registered {} component handlers", count);
        } catch (Exception e) {
            Skyblock.getLogger().error("Failed to scan component handlers", e);
        }
    }
}