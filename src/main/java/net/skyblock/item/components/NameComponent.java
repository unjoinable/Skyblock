package net.skyblock.item.components;

import net.skyblock.item.component.ItemComponent;

/**
 * Component defining the display name of an item.
 * <p>
 * This component contains the name that will be shown to players
 * when viewing the item in-game.
 */
public record NameComponent(String name) implements ItemComponent {

    /**
     * Creates a new NameComponent with the specified name.
     *
     * @param name The display name for this item
     * @return A new NameComponent with the updated name
     */
    public NameComponent with(String name) {
        return new NameComponent(name);
    }
}