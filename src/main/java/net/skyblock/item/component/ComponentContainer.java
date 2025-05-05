package net.skyblock.item.component;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minestom.server.event.EventDispatcher;
import net.skyblock.event.custom.ComponentAddEvent;
import net.skyblock.event.custom.ComponentRemoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Immutable, thread-safe container for {@link ItemComponent}s.
 *
 * <p>
 * This class stores components keyed by their declared type,
 * and supports efficient structural sharing. Use {@link #builder()} to
 * create a new builder or modify an existing container.
 */
@SuppressWarnings("unchecked")
public final class ComponentContainer {
    private static final Object2ObjectMap<Class<? extends ItemComponent>, ItemComponent> EMPTY =
            Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>());

    private final Object2ObjectMap<Class<? extends ItemComponent>, ItemComponent> components;

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
            @NotNull Object2ObjectMap<Class<? extends ItemComponent>, ItemComponent> components) {
        this.components = Object2ObjectMaps.unmodifiable(components);
    }

    /**
     * Creates a new builder for creating or modifying component containers.
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new builder initialized with the components from this container.
     * @return a new builder with this container's components
     */
    public Builder toBuilder() {
        return new Builder(this);
    }

    /**
     * Checks whether this container has any components.
     * @return true if this container has no components, false otherwise
     */
    public boolean isEmpty() {
        return components.isEmpty();
    }

    /**
     * Returns a new container with the given component added or replaced.
     *
     * @param component the component to add (must not be null)
     * @return a new container with the component
     */
    public ComponentContainer with(@NotNull ItemComponent component) {
        Objects.requireNonNull(component);
        Class<? extends ItemComponent> type = component.getType();

        if (components.isEmpty()) {
            var newMap = new Object2ObjectOpenHashMap<Class<? extends ItemComponent>, ItemComponent>(1);
            newMap.put(type, component);
            return new ComponentContainer(newMap);
        }

        // Only copy if necessary
        ItemComponent existing = components.get(type);
        if (Objects.equals(existing, component)) {
            return this;
        }

        var newMap = new Object2ObjectOpenHashMap<>(components);
        newMap.put(type, component);
        return new ComponentContainer(newMap);
    }

    /**
     * Returns a new container with the given component type removed.
     *
     * @param type the component class to remove
     * @return a new container without the component
     */
    public ComponentContainer without(@NotNull Class<? extends ItemComponent> type) {
        Objects.requireNonNull(type);

        ItemComponent removed = components.get(type);
        if (removed == null) {
            return this;
        }

        var newMap = new Object2ObjectOpenHashMap<>(components);
        newMap.remove(type);
        return new ComponentContainer(newMap);
    }

    /**
     * Gets a component by its type.
     * @param type the component class to retrieve
     * @param <T> the expected component type
     * @return an Optional containing the component if present
     */
    public <T extends ItemComponent> Optional<T> get(@NotNull Class<T> type) {
        Objects.requireNonNull(type);
        ItemComponent component = components.get(type);
        return component != null ? Optional.of((T) component) : Optional.empty();
    }

    /**
     * Checks whether a component of the given type exists.
     * @param type the component class to check
     * @return true if present, false otherwise
     */
    public boolean contains(@NotNull Class<? extends ItemComponent> type) {
        Objects.requireNonNull(type);
        return components.containsKey(type);
    }

    /**
     * Returns an unmodifiable view of the internal component map.
     * @return unmodifiable component map
     */
    public Object2ObjectMap<Class<? extends ItemComponent>, ItemComponent> asMap() {
        return components;
    }

    /**
     * Creates a deep copy of the component container.
     * This is useful when you need a fresh container with the same components.
     *
     * @return a new ComponentContainer with copies of all components
     */
    public @NotNull ComponentContainer copy() {
        if (components.isEmpty()) {
            return new ComponentContainer();
        }

        var newMap = new Object2ObjectOpenHashMap<Class<? extends ItemComponent>, ItemComponent>();
        newMap.putAll(components);
        return new ComponentContainer(newMap);
    }

    /**
     * Mutable builder for creating {@link ComponentContainer} instances.
     * The builder allows adding and removing components before creating
     * an immutable ComponentContainer.
     */
    public static final class Builder {
        private final Object2ObjectOpenHashMap<Class<? extends ItemComponent>, ItemComponent> components;

        /**
         * Creates a new empty builder.
         */
        public Builder() {
            this.components = new Object2ObjectOpenHashMap<>();
        }

        /**
         * Creates a new builder with components from an existing container.
         * @param container the existing container to copy components from
         */
        public Builder(ComponentContainer container) {
            this.components = new Object2ObjectOpenHashMap<>(container.components);
        }

        /**
         * Adds or replaces a component in the builder.
         * @param component the component to add
         * @return this builder for chaining
         */
        public Builder with(@NotNull ItemComponent component) {
            Objects.requireNonNull(component);
            ComponentAddEvent event = new ComponentAddEvent(this, component);
            EventDispatcher.callCancellable(event, () -> components.put(component.getType(), component));
            return this;
        }

        /**
         * Removes a component from the builder.
         * @param type the component type to remove
         * @return this builder for chaining
         */
        public Builder without(@NotNull Class<? extends ItemComponent> type) {
            Objects.requireNonNull(type);
            if (components.containsKey(type)) {
                ComponentRemoveEvent event = new ComponentRemoveEvent(this, components.get(type));
                EventDispatcher.callCancellable(event, () -> components.remove(type));
            }
            return this;
        }

        /**
         * Checks if a component is present in the builder.
         * @param type the component type to check for
         * @return true if the component is present, false otherwise
         */
        public boolean contains(@NotNull Class<? extends ItemComponent> type) {
            Objects.requireNonNull(type);
            return components.containsKey(type);
        }

        /**
         * Gets a component by its type.
         * @param type the component class to retrieve
         * @param <T> the expected component type
         * @return an Optional containing the component if present
         */
        public <T extends ItemComponent> Optional<T> get(@NotNull Class<T> type) {
            Objects.requireNonNull(type);
            ItemComponent component = components.get(type);
            return component != null ? Optional.of((T) component) : Optional.empty();
        }

        /**
         * Builds an immutable ComponentContainer with the current components.
         * @return a new immutable ComponentContainer
         */
        public ComponentContainer build() {
            if (components.isEmpty()) {
                return new ComponentContainer();
            }
            return new ComponentContainer(new Object2ObjectOpenHashMap<>(components));
        }
    }
}