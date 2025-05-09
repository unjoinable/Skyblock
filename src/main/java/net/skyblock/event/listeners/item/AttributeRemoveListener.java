package net.skyblock.event.listeners.item;

import net.minestom.server.event.EventListener;
import net.skyblock.event.custom.AttributeRemoveEvent;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemAttribute;
import net.skyblock.item.attribute.base.StatModifierAttribute;
import net.skyblock.item.attribute.impl.StatsAttribute;
import org.jetbrains.annotations.NotNull;

public class AttributeRemoveListener implements EventListener<AttributeRemoveEvent> {
    @Override
    public @NotNull Class<AttributeRemoveEvent> eventType() {
        return AttributeRemoveEvent.class;
    }

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