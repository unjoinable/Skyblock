package net.skyblock.item.component.trait;

import net.skyblock.item.component.Component;

/**
 * Interface for components that have a priority in lore ordering.
 */
public interface LorePrioritized extends Component {
    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     */
    int lorePriority();
}
