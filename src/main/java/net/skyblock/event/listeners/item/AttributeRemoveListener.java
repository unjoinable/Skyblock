package net.skyblock.event.listeners.item;

import net.minestom.server.event.EventListener;
import net.skyblock.event.custom.AttributeRemoveEvent;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.StatModifierAttribute;
import net.skyblock.item.attribute.impl.StatsAttribute;
import org.jetbrains.annotations.NotNull;

public class AttributeRemoveListener implements EventListener<AttributeRemoveEvent> {
    /**
     * Returns the event type that this listener handles.
     *
     * @return the class object representing {@code AttributeRemoveEvent}
     */
    @Override
    public @NotNull Class<AttributeRemoveEvent> eventType() {
        return AttributeRemoveEvent.class;
    }

    /**
     * Handles an {@link AttributeRemoveEvent} by removing a stat modifier attribute from the attribute container.
     *
     * If the attribute to be removed is a {@link StatModifierAttribute}, updates the container's {@link StatsAttribute}
     * to exclude the specified modifier. Always returns {@link Result#SUCCESS} after processing.
     *
     * @param event the attribute removal event to process
     * @return {@link Result#SUCCESS} after handling the event
     */
    @Override
    public @NotNull Result run(@NotNull AttributeRemoveEvent event) {
        AttributeContainer.Builder container = event.getContainer();
        ItemAttribute attribute = event.getAttribute();

        if (attribute instanceof StatModifierAttribute modifier) {
            StatsAttribute stats = container
                    .get(StatsAttribute.class)
                    .orElse(new StatsAttribute());
            StatsAttribute updatedStats = stats.without(modifier);
            container.with(updatedStats);
        }
        return Result.SUCCESS;
    }
}