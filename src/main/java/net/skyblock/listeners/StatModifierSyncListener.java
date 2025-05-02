package net.skyblock.listeners;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.item.component.event.ComponentChangeListener;
import net.skyblock.item.component.impl.StatsComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that synchronizes StatModifierComponents with the master StatsComponent.
 * This ensures that when modifiers are added or removed, they are reflected in the StatsComponent.
 */
public final class StatModifierSyncListener implements ComponentChangeListener {

    @Override
    public ComponentContainer onComponentAdded(@NotNull ComponentContainer container, @NotNull ItemComponent component) {
        if (component instanceof ModifierComponent modifier) {
            StatsComponent stats = container
                    .get(StatsComponent.class)
                    .orElse(new StatsComponent());

            StatsComponent updatedStats = stats.withModifier(modifier);
            return container.with(updatedStats);
        }
        return container;
    }

    @Override
    public ComponentContainer onComponentRemoved(@NotNull ComponentContainer container, @NotNull ItemComponent component) {
        if (component instanceof ModifierComponent modifier) {
            StatsComponent stats = container
                    .get(StatsComponent.class)
                    .orElse(new StatsComponent());
            StatsComponent updatedStats = stats.withoutModifier(modifier);
            return container.with(updatedStats);
        }
        return container;
    }
}