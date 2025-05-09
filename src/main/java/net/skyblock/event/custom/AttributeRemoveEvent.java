package net.skyblock.event.custom;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when an attribute is removed from a {@link AttributeContainer.Builder}.
 * <p>
 * This event is fired before the attribute is actually removed, allowing listeners
 * to modify or reject the attribute removal.
 */
public class AttributeRemoveEvent implements CancellableEvent {
    private final AttributeContainer.Builder builder;
    private final ItemAttribute attribute;
    private boolean cancelled;

    /**
     * Constructs an event representing the impending removal of an attribute from an attribute container builder.
     *
     * @param builder the attribute container builder involved in the removal
     * @param attribute the attribute intended for removal; may be null if not present in the builder
     */
    public AttributeRemoveEvent(@NotNull AttributeContainer.Builder builder, @NotNull ItemAttribute attribute) {
        this.builder = builder;
        this.attribute = attribute;
    }

    /**
     * Returns the attribute container builder involved in the attribute removal event.
     *
     * @return the builder from which the attribute is being removed
     */
    public @NotNull AttributeContainer.Builder getContainer() {
        return this.builder;
    }

    /**
     * Returns the attribute intended for removal from the container.
     *
     * @return the attribute being removed, or null if not present
     */
    public @NotNull ItemAttribute getAttribute() {
        return this.attribute;
    }

    /**
     * Returns whether the attribute removal event has been cancelled.
     *
     * @return true if the event is cancelled and the attribute removal should not proceed
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancel true to cancel the event and prevent the attribute removal, false to allow it
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}