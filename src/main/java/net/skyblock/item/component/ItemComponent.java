package net.skyblock.item.component;

import net.skyblock.Skyblock;
import net.skyblock.registry.HandlerRegistry;

/**
 * Base marker interface for all components.
 * Implementations must be immutable and thread-safe.
 */
public interface ItemComponent {

    /**
     * Returns the handler for the current component class.
     * This method uses reflection to determine the component type.
     *
     * @return The handler for this component type
     */
    default ItemComponentHandler<?> getHandler() {
        Class<? extends ItemComponent> componentClass = this.getType();
        HandlerRegistry registry = Skyblock.getInstance().getHandlerRegistry();
        return registry.getHandler(componentClass);
    }

    /**
     * Returns the component type used for storage and retrieval.
     * Defaults to the implementing class.
     * Override if your class structure needs custom keying (e.g. anonymous or shared base types).
     */
    @SuppressWarnings("unchecked")
    default <T extends ItemComponent> Class<T> getType() {
        return (Class<T>) this.getClass();
    }
}