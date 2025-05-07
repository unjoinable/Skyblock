package net.skyblock.item.component;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minestom.server.event.EventDispatcher;
import net.skyblock.component.ComponentContainer;
import net.skyblock.event.custom.ComponentAddEvent;
import net.skyblock.event.custom.ComponentRemoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link ComponentContainer} for item components.
 *
 * <p>
 * This class stores {@link ItemComponent}s keyed by their declared type,
 * and supports efficient structural sharing through its builder.
 * <p>
 */
public final class ItemComponents implements ComponentContainer<ItemComponent> {
    private static final Object2ObjectMap<Class<? extends ItemComponent>, ItemComponent> EMPTY =
            Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>());

    private final Object2ObjectMap<Class<? extends ItemComponent>, ItemComponent> components;

    /**
     * Creates an empty component container.
     */
    public ItemComponents() {
        this.components = EMPTY;
    }

    /**
     * Internal constructor for modified containers.
     * @param components backing component map (will be wrapped as unmodifiable)
     */
    private ItemComponents(
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

    @Override
    public @NotNull Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean isEmpty() {
        return components.isEmpty();
    }

    @Override @SuppressWarnings("unchecked")
    public <C extends ItemComponent> Optional<C> get(@NotNull Class<C> type) {
        Objects.requireNonNull(type);
        ItemComponent component = components.get(type);
        return component != null ? Optional.of((C) component) : Optional.empty();
    }

    @Override
    public boolean contains(@NotNull Class<? extends ItemComponent> type) {
        Objects.requireNonNull(type);
        return components.containsKey(type);
    }

    @Override
    public Object2ObjectMap<Class<? extends ItemComponent>, ItemComponent> asMap() {
        return components;
    }

    /**
     * Builder implementation for ItemComponents.
     *
     * <p>
     * This class provides a fluent API for building {@link ItemComponents} instances.
     * The builder is not thread-safe, but the resulting ItemComponents is immutable
     * and thread-safe.
     */
    public static final class Builder implements ComponentContainer.Builder<ItemComponent, ItemComponents> {
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
        public Builder(ItemComponents container) {
            this.components = new Object2ObjectOpenHashMap<>(container.components);
        }

        @Override
        public @NotNull Builder with(@NotNull ItemComponent component) {
            Objects.requireNonNull(component);

            // Check if component already exists and remove it first
            Class<? extends ItemComponent> type = component.getType();
            if (components.containsKey(type)) {
                ComponentRemoveEvent removeEvent = new ComponentRemoveEvent(this, components.get(type));
                EventDispatcher.callCancellable(removeEvent, () -> components.remove(type));
                // If removal was cancelled, don't proceed with adding the new component
                if (removeEvent.isCancelled()) {
                    return this;
                }
            }

            // Now add the new component
            ComponentAddEvent addEvent = new ComponentAddEvent(this, component);
            EventDispatcher.callCancellable(addEvent, () -> components.put(type, component));
            return this;
        }

        @Override
        public @NotNull Builder without(@NotNull Class<? extends ItemComponent> type) {
            Objects.requireNonNull(type);
            if (components.containsKey(type)) {
                ComponentRemoveEvent event = new ComponentRemoveEvent(this, components.get(type));
                EventDispatcher.callCancellable(event, () -> components.remove(type));
            }
            return this;
        }

        @Override
        public boolean contains(@NotNull Class<? extends ItemComponent> type) {
            Objects.requireNonNull(type);
            return components.containsKey(type);
        }

        @Override @SuppressWarnings("unchecked")
        public <C extends ItemComponent> Optional<C> get(@NotNull Class<C> type) {
            Objects.requireNonNull(type);
            ItemComponent component = components.get(type);
            return component != null ? Optional.of((C) component) : Optional.empty();
        }

        @Override
        public @NotNull ItemComponents build() {
            if (components.isEmpty()) {
                return new ItemComponents();
            }
            return new ItemComponents(new Object2ObjectOpenHashMap<>(components));
        }
    }
}