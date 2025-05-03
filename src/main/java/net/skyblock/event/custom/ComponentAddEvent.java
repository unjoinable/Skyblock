package net.skyblock.event.custom;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a component is added to a {@link ComponentContainer.Builder}.
 * <p>
 * This event is fired before the component is actually added, allowing listeners
 * to modify or reject the component addition.
 */
public class ComponentAddEvent implements CancellableEvent {
    private final ComponentContainer.Builder builder;
    private final ItemComponent component;
    private boolean cancelled;

    /**
     * Creates a new ComponentAddEvent.
     *
     * @param builder   the builder to which the component is being added
     * @param component the component being added
     */
    public ComponentAddEvent(@NotNull ComponentContainer.Builder builder, @NotNull ItemComponent component) {
        this.builder = builder;
        this.component = component;
    }

    /**
     * Gets the builder to which the component is being added.
     *
     * @return the component container builder
     */
    public @NotNull ComponentContainer.Builder getContainer() {
        return this.builder;
    }

    /**
     * Gets the component being added.
     *
     * @return the component
     */
    public @NotNull ItemComponent getComponent() {
        return this.component;
    }

    /**
     * Gets if the {@link Event} should be cancelled or not.
     *
     * @return true if the event should be cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Marks the {@link Event} as cancelled or not.
     *
     * @param cancel true if the event should be cancelled, false otherwise
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
