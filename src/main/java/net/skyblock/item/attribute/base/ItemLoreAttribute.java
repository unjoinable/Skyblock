package net.skyblock.item.attribute.base;

import net.kyori.adventure.text.Component;
import net.skyblock.item.attribute.AttributeContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an {@link ItemAttribute} that provides descriptive text for item lore.
 * Classes implementing this contribute lines to the item's tooltip using rich text components.
 */
public interface ItemLoreAttribute extends ItemAttribute {

    /**
     * Generates the list of lore lines for this attribute as Adventure text components.
     *
     * @return A non-null list of {@link Component}s, representing lore lines. Return an empty list if no lines are provided.
     */
    @NotNull List<Component> loreLines(@NotNull AttributeContainer container);

    /**
     * Returns the priority for sorting this attribute's lore lines among other lore attributes.
     * Lower values typically mean higher priority and the lore will appear earlier in the tooltip.
     *
     * @return The sorting priority.
     */
    int priority();
}