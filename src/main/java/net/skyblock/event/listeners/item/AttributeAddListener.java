package net.skyblock.event.listeners.item;

import net.minestom.server.event.EventListener;
import net.skyblock.event.custom.AttributeAddEvent;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.StatModifierAttribute;
import net.skyblock.item.attribute.impl.StatsAttribute;
import org.jetbrains.annotations.NotNull;

public class AttributeAddListener implements EventListener<AttributeAddEvent> {

    /**
     * Returns the event type handled by this listener.
     *
     * @return the {@code AttributeAddEvent} class
     */
    @Override
    public @NotNull Class<AttributeAddEvent> eventType() {
        return AttributeAddEvent.class;
    }

    /**
     * Processes an AttributeAddEvent by applying a StatModifierAttribute to the container's StatsAttribute if present.
     *
     * If the event's attribute is a StatModifierAttribute, updates or creates the StatsAttribute in the container by applying the modifier.
     *
     * @param event the AttributeAddEvent to process
     * @return Result.SUCCESS after processing the event
     */
    @Override
    public @NotNull Result run(@NotNull AttributeAddEvent event) {
        AttributeContainer.Builder container = event.getContainer();
        ItemAttribute attribute = event.getAttribute();

        if (attribute instanceof StatModifierAttribute modifier) {
            StatsAttribute stats = container
                    .get(StatsAttribute.class)
                    .orElse(new StatsAttribute());

            StatsAttribute updatedStats = stats.with(modifier);
            container.with(updatedStats);
        }
        return Result.SUCCESS;
    }
}