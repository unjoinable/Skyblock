package net.skyblock.item.component.event;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ComponentContainer;

/**
 * Interface for receiving notifications when components change.
 */
public interface ComponentChangeListener {
    /**
     * Called when a component is added to a container.
     * @param container The container that now contains the component
     * @param component The component that was added
     * @return The container with changed values
     */
    ComponentContainer onComponentAdded(ComponentContainer container, ItemComponent component);

    /**
     * Called when a component is removed from a container.
     * @param container The container that no longer contains the component
     * @param component The component that was removed
     * @return The container with changed values
     */
    ComponentContainer onComponentRemoved(ComponentContainer container, ItemComponent component);
}