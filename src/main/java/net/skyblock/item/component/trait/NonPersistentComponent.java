package net.skyblock.item.component.trait;

import net.skyblock.item.component.Component;

/**
 * A component that is never saved or serialized.
 * Exists only in memory during runtime.
 */
public interface NonPersistentComponent extends Component {
    // Marker only — no methods needed
}