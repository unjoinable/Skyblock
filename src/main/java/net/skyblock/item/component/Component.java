package net.skyblock.item.component;

/**
 * Base marker interface for all components.
 * Implementations must be immutable and thread-safe.
 */
public interface Component {

    /**
     * Returns the component type used for storage and retrieval.
     * Defaults to the implementing class.
     * Override if your class structure needs custom keying (e.g. anonymous or shared base types).
     */
    default Class<? extends Component> getType() {
        return this.getClass();
    }
}