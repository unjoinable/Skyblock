package net.skyblock.event.listeners.item;

import net.minestom.server.event.EventListener;
import net.skyblock.event.custom.ComponentAddEvent;
import net.skyblock.item.component.ItemComponents;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.item.component.definition.StatsComponent;
import org.jetbrains.annotations.NotNull;

public class ComponentAddListener implements EventListener<ComponentAddEvent> {

    @Override
    public @NotNull Class<ComponentAddEvent> eventType() {
        return ComponentAddEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull ComponentAddEvent event) {
        ItemComponents.Builder container = event.getContainer();
        ItemComponent component = event.getComponent();

        if (component instanceof ModifierComponent modifier) {
            StatsComponent stats = container
                    .get(StatsComponent.class)
                    .orElse(new StatsComponent());

            StatsComponent updatedStats = stats.withModifier(modifier);
            container.with(updatedStats);
        }
        return Result.SUCCESS;
    }
}
