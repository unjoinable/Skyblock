package net.skyblock.event.listeners.item;

import net.minestom.server.event.EventListener;
import net.skyblock.event.custom.ComponentRemoveEvent;
import net.skyblock.item.component.ItemComponents;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.item.component.definition.StatsComponent;
import org.jetbrains.annotations.NotNull;

public class ComponentRemoveListener implements EventListener<ComponentRemoveEvent> {
    @Override
    public @NotNull Class<ComponentRemoveEvent> eventType() {
        return ComponentRemoveEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull ComponentRemoveEvent event) {
        ItemComponents.Builder container = event.getContainer();
        ItemComponent component = event.getComponent();

        if (component instanceof ModifierComponent modifier) {
            StatsComponent stats = container
                    .get(StatsComponent.class)
                    .orElse(new StatsComponent());
            StatsComponent updatedStats = stats.withoutModifier(modifier);
            container.with(updatedStats);
        }
        return Result.SUCCESS;
    }
}
