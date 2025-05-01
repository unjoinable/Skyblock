package net.skyblock.item.component.impl;

import net.skyblock.item.component.ModifierComponent;

/**
 * Component tracking the number of Hot Potato Books applied to an item.
 * <p>
 * Hot Potato Books are consumable items that can be applied to equipment
 * to improve their stats, up to a maximum number of applications.
 */
public record HotPotatoBookComponent(int count) implements ModifierComponent {

    /**
     * Creates a new HotPotatoBookComponent with the specified count.
     *
     * @param count The number of Hot Potato Books applied
     * @return A new HotPotatoBookComponent with the updated count
     */
    public HotPotatoBookComponent with(int count) {
        return new HotPotatoBookComponent(count);
    }
}