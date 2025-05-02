package net.skyblock.item.component;

import net.skyblock.Skyblock;
import net.skyblock.item.component.trait.ModifierHandler;
import net.skyblock.registry.HandlerRegistry;

/**
 * An interface that represents components which contain data for item stat modifications.
 * <p>
 * This interface extends {@link ItemComponent} and serves as a container for data related to
 * stat modifications that can be applied to items. Components implementing this interface
 * are specifically designed to hold and manage information about how item statistics
 * should be modified.
 * </p>
 * <p>
 * The ModifierComponent doesn't apply modifications directly; rather, it stores the data
 * that defines the modification parameters
 * </p>
 * <p>
 * Implementations of this interface are typically used by the stat calculation system
 * to retrieve modification data when computing final item statistics.
 * </p>
 *
 * @see ItemComponent The parent interface that all item components must implement
 */
public interface ModifierComponent extends ItemComponent {
    // This interface doesn't define additional methods as it's for data storage
    // Implementing classes are expected to provide their own methods for accessing
    // and manipulating the modifier data they contain

    /**
     * Helper method to get the specific ModifierHandler for this component
     * @return The ModifierHandler specialized for this component type
     */
    default ModifierHandler<?> getModifierHandler() {
        Class<? extends ModifierComponent> componentClass = this.getType();
        HandlerRegistry registry = Skyblock.getInstance().getHandlerRegistry();
        return (ModifierHandler<?>) registry.getHandler(componentClass);
    }
}