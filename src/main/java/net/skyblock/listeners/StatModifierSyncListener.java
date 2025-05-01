package net.skyblock.listeners;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.event.ComponentChangeListener;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that synchronizes StatModifierComponents with the master StatsComponent.
 * This ensures that when modifiers are added or removed, they are reflected in the StatsComponent.
 */
public final class StatModifierSyncListener implements ComponentChangeListener {

    @Override
    public ComponentContainer onComponentAdded(@NotNull ComponentContainer container, @NotNull ItemComponent component) {
//        if (component instanceof StatModifierComponent modifier) {
//            // Get or create the StatsComponent
//            StatsComponent stats = container
//                    .get(StatsComponent.class)
//                    .orElse(new StatsComponent());
//
//            // Add the modifier to the stats component
//            StatsComponent updatedStats = stats.withModifier(modifier);
//
//            // Return container with updated StatsComponent
//            return container.with(updatedStats);
//        }
        return container;
    }

    @Override
    public ComponentContainer onComponentRemoved(@NotNull ComponentContainer container, @NotNull ItemComponent component) {
//        if (component instanceof StatModifierComponent modifier) {
//            // Only proceed if we have a StatsComponent
//            if (container.contains(StatsComponent.class)) {
//                return container.get(StatsComponent.class).map(stats -> {
//                    // Create a new StatsComponent without the modifier type
//                    StatsComponent updatedStats = stats.withoutModifier(modifier.getModifierType());
//                    return container.with(updatedStats);
//                }).orElse(container); // This should never happen due to our check above
//            }
//        }
        return container;
    }
}