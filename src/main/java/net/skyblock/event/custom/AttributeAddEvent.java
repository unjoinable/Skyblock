package net.skyblock.event.custom;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when an attribute is added to a {@link AttributeContainer.Builder}.
 * <p>
 * This event is fired before the attribute is actually added, allowing listeners
 * to modify or reject the attribute addition.
 */
public class AttributeAddEvent implements CancellableEvent {
    private final AttributeContainer.Builder builder;
    private final ItemAttribute attribute;
    private boolean cancelled;

    /**
     * Creates a new AttributeAddEvent.
     *
     * @param builder   the builder to which the attribute is being added
     * @param attribute the attribute being added
     */
    public AttributeAddEvent(@NotNull AttributeContainer.Builder builder, @NotNull ItemAttribute attribute) {
        this.builder = builder;
        this.attribute = attribute;
    }

    /**
     * Gets the builder to which the attribute is being added.
     *
     * @return the attribute container builder
     */
    public @NotNull AttributeContainer.Builder getContainer() {
        return this.builder;
    }

    /**
     * Gets the attribute being added.
     *
     * @return the attribute
     */
    public @NotNull ItemAttribute getAttribute() {
        return this.attribute;
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