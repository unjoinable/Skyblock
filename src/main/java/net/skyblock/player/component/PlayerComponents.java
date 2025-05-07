package net.skyblock.player.component;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.skyblock.component.ComponentContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link ComponentContainer} for player components.
 *
 * <p>
 * This class stores {@link PlayerComponent}s keyed by their declared type,
 * and supports efficient structural sharing through its builder.
 * <p>
 */
public final class PlayerComponents implements ComponentContainer<PlayerComponent> {
    private static final Object2ObjectMap<Class<? extends PlayerComponent>, PlayerComponent> EMPTY =
            Object2ObjectMaps.unmodifiable(new Object2ObjectOpenHashMap<>());

    private final Object2ObjectMap<Class<? extends PlayerComponent>, PlayerComponent> components;

    /**
     * Creates an empty component container.
     */
    public PlayerComponents() {
        this.components = EMPTY;
    }

    /**
     * Internal constructor for modified containers.
     * @param components backing component map (will be wrapped as unmodifiable)
     */
    private PlayerComponents(
            @NotNull Object2ObjectMap<Class<? extends PlayerComponent>, PlayerComponent> components) {
        this.components = Object2ObjectMaps.unmodifiable(components);
    }

    /**
     * Updates all components by calling their tick method.
     * This should be called every game tick for active players.
     */
    public void update() {
        for (PlayerComponent component : components.values()) {
            component.tick();
        }
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
    public <C extends PlayerComponent> Optional<C> get(@NotNull Class<C> type) {
        Objects.requireNonNull(type);
        PlayerComponent component = components.get(type);
        return component != null ? Optional.of((C) component) : Optional.empty();
    }

    @Override
    public boolean contains(@NotNull Class<? extends PlayerComponent> type) {
        Objects.requireNonNull(type);
        return components.containsKey(type);
    }

    @Override
    public Object2ObjectMap<Class<? extends PlayerComponent>, PlayerComponent> asMap() {
        return components;
    }

    /**
     * Builder implementation for PlayerComponents.
     *
     * <p>
     * This class provides a fluent API for building {@link PlayerComponents} instances.
     * The builder is not thread-safe, but the resulting PlayerComponents is immutable
     * and thread-safe.
     */
    public static final class Builder implements ComponentContainer.Builder<PlayerComponent, PlayerComponents> {
        private final Object2ObjectOpenHashMap<Class<? extends PlayerComponent>, PlayerComponent> components;

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
        public Builder(PlayerComponents container) {
            this.components = new Object2ObjectOpenHashMap<>(container.components);
        }

        @Override
        public @NotNull Builder with(@NotNull PlayerComponent component) {
            Objects.requireNonNull(component);

            // Check if component already exists and remove it first
            Class<? extends PlayerComponent> type = component.getType();
            if (components.containsKey(type)) {
                // No event handling for now as per requirements
                components.remove(type);
            }

            // Now add the new component
            components.put(type, component);
            return this;
        }

        @Override
        public @NotNull Builder without(@NotNull Class<? extends PlayerComponent> type) {
            Objects.requireNonNull(type);
            components.remove(type);
            return this;
        }

        @Override
        public boolean contains(@NotNull Class<? extends PlayerComponent> type) {
            Objects.requireNonNull(type);
            return components.containsKey(type);
        }

        @Override @SuppressWarnings("unchecked")
        public <C extends PlayerComponent> Optional<C> get(@NotNull Class<C> type) {
            Objects.requireNonNull(type);
            PlayerComponent component = components.get(type);
            return component != null ? Optional.of((C) component) : Optional.empty();
        }

        @Override
        public @NotNull PlayerComponents build() {
            if (components.isEmpty()) {
                return new PlayerComponents();
            }
            return new PlayerComponents(new Object2ObjectOpenHashMap<>(components));
        }
    }
}