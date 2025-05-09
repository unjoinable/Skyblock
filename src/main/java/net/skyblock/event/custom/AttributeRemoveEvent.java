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
     * Gets the builder from which the attribute is being removed.
     *
     * @return the attribute container builder
     */
    public @NotNull AttributeContainer.Builder getContainer() {
        return this.builder;
    }

    /**
     * Gets the attribute being removed, if it exists.
     * This may be null if the attribute was not present.
     *
     * @return the attribute being removed, or null
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