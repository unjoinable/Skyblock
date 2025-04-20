package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;

/**
 * A component that is never saved or serialized.
 * Exists only in memory during runtime.
 */
public interface TransientComponent extends Component {
    // Marker only — no methods needed
}
