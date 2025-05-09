package net.skyblock.event.custom;

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
     * Constructs an event representing the pending addition of an attribute to an attribute container builder.
     *
     * @param builder the attribute container builder involved in the event
     * @param attribute the item attribute that is about to be added
     */
    public AttributeAddEvent(@NotNull AttributeContainer.Builder builder, @NotNull ItemAttribute attribute) {
        this.builder = builder;
        this.attribute = attribute;
    }

    /**
     * Returns the attribute container builder involved in this event.
     *
     * @return the builder to which the attribute is being added
     */
    public @NotNull AttributeContainer.Builder getContainer() {
        return this.builder;
    }

    /**
     * Returns the attribute that is being added to the container during this event.
     *
     * @return the attribute involved in the addition event
     */
    public @NotNull ItemAttribute getAttribute() {
        return this.attribute;
    }

    /**
     * Returns whether the event has been marked as cancelled.
     *
     * @return true if the event is cancelled; false otherwise
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancel true to cancel the event and prevent the attribute from being added; false to allow the addition
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}