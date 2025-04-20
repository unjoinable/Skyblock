package com.github.unjoinable.skyblock.item.component;

import com.github.unjoinable.skyblock.item.component.event.ComponentChangeListener;
import com.github.unjoinable.skyblock.registry.Registry;
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

    private final Object2ObjectMap<Class<? extends Component>, Component> components;
    private final List<ComponentChangeListener> listeners;

    /**
     * Creates an empty component container.
     */
    public ComponentContainer() {
        this.components = EMPTY;
        this.listeners = new CopyOnWriteArrayList<>();
    }

    /**
     * Internal constructor for modified containers.
     * @param components backing component map (will be wrapped as unmodifiable)
     * @param listeners list of event listeners to maintain
     */
    private ComponentContainer(
            @NotNull Object2ObjectMap<Class<? extends Component>, Component> components,
            @NotNull List<ComponentChangeListener> listeners) {
        this.components = Object2ObjectMaps.unmodifiable(components);
        this.listeners = new CopyOnWriteArrayList<>(listeners);
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
            var result = new ComponentContainer(newMap, listeners);
            notifyComponentAdded(result, component);
            return result;
        }

        // Only copy if necessary
        Component existing = components.get(type);
        if (Objects.equals(existing, component)) {
            return this;
        }

        var newMap = new Object2ObjectOpenHashMap<>(components);
        newMap.put(type, component);
        var result = new ComponentContainer(newMap, listeners);

        if (existing != null) {
            notifyComponentRemoved(result, existing);
        }
        notifyComponentAdded(result, component);

        return result;
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
        var result = new ComponentContainer(newMap, listeners);
        notifyComponentRemoved(result, removed);
        return result;
    }

    /**
     * Adds an event listener to this container.
     * @param listener the listener to add
     * @return a new container with the listener added
     */
    public ComponentContainer addListener(@NotNull ComponentChangeListener listener) {
        Objects.requireNonNull(listener);
        List<ComponentChangeListener> newListeners = new ArrayList<>(listeners);
        newListeners.add(listener);
        return new ComponentContainer(components, newListeners);
    }

    /**
     * Removes an event listener from this container.
     * @param listener the listener to remove
     * @return a new container without the listener
     */
    public ComponentContainer removeListener(@NotNull ComponentChangeListener listener) {
        Objects.requireNonNull(listener);
        List<ComponentChangeListener> newListeners = new ArrayList<>(listeners);
        if (newListeners.remove(listener)) {
            return new ComponentContainer(components, newListeners);
        }
        return this;
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
     * Gets a component by its registry ID.
     * @param registryId the registered component ID
     * @param <T> the expected component type
     * @return an Optional containing the component if present
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Optional<T> getByRegistryId(@NotNull String registryId) {
        Objects.requireNonNull(registryId);
        Class<? extends Component> type = Registry.COMPONENT_REGISTRY.get(registryId);
        if (type == null) {
            return Optional.empty();
        }
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

    private void notifyComponentAdded(ComponentContainer container, Component component) {
        for (ComponentChangeListener listener : listeners) {
            listener.onComponentAdded(container, component);
        }
    }

    private void notifyComponentRemoved(ComponentContainer container, Component component) {
        for (ComponentChangeListener listener : listeners) {
            listener.onComponentRemoved(container, component);
        }
    }
}