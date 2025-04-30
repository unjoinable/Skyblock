package net.skyblock.item.component;

/**
 * Base marker interface for all components.
 * Implementations must be immutable and thread-safe.
 */
public interface ItemComponent {

    /**
     * Returns the component type used for storage and retrieval.
     * Defaults to the implementing class.
     * Override if your class structure needs custom keying (e.g. anonymous or shared base types).
     */
    default Class<? extends ItemComponent> getType() {
        return this.getClass();
    }
}