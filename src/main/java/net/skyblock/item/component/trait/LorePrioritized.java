package net.skyblock.item.component.trait;

import net.skyblock.item.component.ItemComponent;

/**
 * Interface for components that have a priority in lore ordering.
 */
public interface LorePrioritized extends ItemComponent {
    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     */
    int lorePriority();
}
