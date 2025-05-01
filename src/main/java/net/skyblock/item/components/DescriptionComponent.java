package net.skyblock.item.components;

import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ItemComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

/**
 * A record component that stores the custom description (lore) text for an item.
 *
 * @param description A non-null list of Adventure text components, each representing 
 *                   a line in the item's lore/description
 */
public record DescriptionComponent(@NotNull List<Component> description) implements ItemComponent {
    /**
     * Creates a new description component with the specified text components.
     *
     * @param description A non-null list of Adventure text components
     */
    public DescriptionComponent {
        description = new ArrayList<>(description); // Defensive Copy
    }

    /**
     * Returns a defensive copy of the description list to prevent external modifications.
     *
     * @return A new copy of the description components list
     */
    @Override
    public @NotNull List<Component> description() {
        return new ArrayList<>(description);
    }
}