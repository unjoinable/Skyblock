package net.skyblock.item.component;

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
}