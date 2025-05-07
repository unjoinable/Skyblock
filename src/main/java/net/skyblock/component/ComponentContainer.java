package net.skyblock.component;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Interface for immutable, thread-safe containers of components.
 *
 * <p>
 * This interface defines read operations for a container that stores components keyed by their type.
 * Modifications are handled through the Builder pattern.
 *
 * @param <T> the base type of components this container can hold
 */
public interface ComponentContainer<T extends Component> {

    /**
     * Checks whether this container has any components.
     * @return true if this container has no components, false otherwise
     */
    boolean isEmpty();

    /**
     * Gets a component by its type.
     * @param type the component class to retrieve
     * @param <C> the expected component type
     * @return an Optional containing the component if present
     */
    <C extends T> Optional<C> get(@NotNull Class<C> type);

    /**
     * Checks whether a component of the given type exists.
     * @param type the component class to check
     * @return true if present, false otherwise
     */
    boolean contains(@NotNull Class<? extends T> type);

    /**
     * Returns an unmodifiable view of the internal component map.
     * @return unmodifiable component map
     */
    Object2ObjectMap<Class<? extends T>, T> asMap();

    /**
     * Creates a new builder for this container type.
     * @return a builder instance that can build this container type
     */
    @NotNull Builder<T, ? extends ComponentContainer<T>> toBuilder();

    /**
     * Interface for mutable builders that create ComponentContainer instances.
     *
     * @param <T> the base type of components this builder can handle
     * @param <C> the concrete type of component container this builder creates
     */
    interface Builder<T extends Component, C extends ComponentContainer<T>> {

        /**
         * Adds or replaces a component in the builder.
         * @param component the component to add
         * @return this builder for chaining
         */
        @NotNull Builder<T, C> with(@NotNull T component);

        /**
         * Removes a component from the builder.
         * @param type the component type to remove
         * @return this builder for chaining
         */
        @NotNull Builder<T, C> without(@NotNull Class<? extends T> type);

        /**
         * Checks if a component is present in the builder.
         * @param type the component type to check for
         * @return true if the component is present, false otherwise
         */
        boolean contains(@NotNull Class<? extends T> type);

        /**
         * Gets a component by its type.
         * @param type the component class to retrieve
         * @param <C2> the expected component type
         * @return an Optional containing the component if present
         */
        <C2 extends T> Optional<C2> get(@NotNull Class<C2> type);

        /**
         * Builds an immutable ComponentContainer with the current components.
         * @return a new immutable ComponentContainer
         */
        @NotNull C build();
    }
}