package net.skyblock.event.listeners.item;

import net.minestom.server.event.EventListener;
import net.skyblock.event.custom.AttributeAddEvent;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.StatModifierAttribute;
import net.skyblock.item.attribute.impl.StatsAttribute;
import org.jetbrains.annotations.NotNull;

public class AttributeAddListener implements EventListener<AttributeAddEvent> {

    @Override
    public @NotNull Class<AttributeAddEvent> eventType() {
        return AttributeAddEvent.class;
    }

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