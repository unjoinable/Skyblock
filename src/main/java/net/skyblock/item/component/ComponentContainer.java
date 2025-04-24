package net.skyblock.item.component;

import net.skyblock.item.component.event.ComponentChangeListener;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Immutable, thread-safe container for {@link Component}s.
 *
 * <p>
 * This class stores components keyed by their declared type,
 * and supports efficient structural sharing (copy-on-write).
 * Use {@link #with(Component)} and {@link #without(Class)} to
 * create modified copies of the container.
 */
public final class ComponentContainer {

    private static final Object2ObjectMap<Class<? extends Component>, Component> EMPTY =
            Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>());

    // Static list of listeners that will be notified of all component changes
    private static final List<ComponentChangeListener> GLOBAL_LISTENERS = new CopyOnWriteArrayList<>();

    private final Object2ObjectMap<Class<? extends Component>, Component> components;

    /**
     * Creates an empty component container.
     */
    public ComponentContainer() {
        this.components = EMPTY;
    }

    /**
     * Internal constructor for modified containers.
     * @param components backing component map (will be wrapped as unmodifiable)
     */
    private ComponentContainer(
            @NotNull Object2ObjectMap<Class<? extends Component>, Component> components) {
        this.components = Object2ObjectMaps.unmodifiable(components);
    }

    /**
     * Returns a new container with the given component added or replaced.
     *
     * @param component the component to add (must not be null)
     * @return a new container with the component
     */
    public ComponentContainer with(@NotNull Component component) {
        Objects.requireNonNull(component);
        Class<? extends Component> type = component.getType();

        if (components.isEmpty()) {
            var newMap = new Object2ObjectOpenHashMap<Class<? extends Component>, Component>(1);
            newMap.put(type, component);
            var result = new ComponentContainer(newMap);
            return notifyComponentAdded(result, component);
        }

        // Only copy if necessary
        Component existing = components.get(type);
        if (Objects.equals(existing, component)) {
            return this;
        }

        var newMap = new Object2ObjectOpenHashMap<>(components);
        newMap.put(type, component);
        var result = new ComponentContainer(newMap);

        if (existing != null) {
            result = notifyComponentRemoved(result, existing);
        }
        return notifyComponentAdded(result, component);
    }

    /**
     * Returns a new container with the given component type removed.
     *
     * @param type the component class to remove
     * @return a new container without the component
     */
    public ComponentContainer without(@NotNull Class<? extends Component> type) {
        Objects.requireNonNull(type);

        Component removed = components.get(type);
        if (removed == null) {
            return this;
        }

        var newMap = new Object2ObjectOpenHashMap<>(components);
        newMap.remove(type);
        var result = new ComponentContainer(newMap);
        return notifyComponentRemoved(result, removed);
    }

    /**
     * Adds a global event listener that will be notified of all component changes.
     * @param listener the listener to add
     */
    public static void addListener(@NotNull ComponentChangeListener listener) {
        Objects.requireNonNull(listener);
        GLOBAL_LISTENERS.add(listener);
    }

    /**
     * Removes a global event listener.
     * @param listener the listener to remove
     * @return true if the listener was removed, false if it wasn't registered
     */
    public static boolean removeListener(@NotNull ComponentChangeListener listener) {
        Objects.requireNonNull(listener);
        return GLOBAL_LISTENERS.remove(listener);
    }

    /**
     * Gets a component by its type.
     * @param type the component class to retrieve
     * @param <T> the expected component type
     * @return an Optional containing the component if present
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Optional<T> get(@NotNull Class<T> type) {
        Objects.requireNonNull(type);
        Component component = components.get(type);
        return component != null ? Optional.of((T) component) : Optional.empty();
    }

    /**
     * Checks whether a component of the given type exists.
     * @param type the component class to check
     * @return true if present, false otherwise
     */
    public boolean contains(@NotNull Class<? extends Component> type) {
        Objects.requireNonNull(type);
        return components.containsKey(type);
    }

    /**
     * Returns an unmodifiable view of the internal component map.
     * @return unmodifiable component map
     */
    public Object2ObjectMap<Class<? extends Component>, Component> asMap() {
        return components;
    }

    private static ComponentContainer notifyComponentAdded(ComponentContainer container, Component component) {
        for (ComponentChangeListener listener : GLOBAL_LISTENERS) {
            container = listener.onComponentAdded(container, component);
        }
        return container;
    }

    private static ComponentContainer notifyComponentRemoved(ComponentContainer container, Component component) {
        for (ComponentChangeListener listener : GLOBAL_LISTENERS) {
            container = listener.onComponentRemoved(container, component);
        }
        return container;
    }
}