package net.unjoinable.item.attribute.traits;

import net.kyori.adventure.text.Component;
import net.unjoinable.item.attribute.AttributeContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an {@link ItemAttribute} that provides descriptive text for item lore.
 * Classes implementing this contribute lines to the item's tooltip using rich text components.
 */
public non-sealed interface LoreAttribute extends ItemAttribute {

    /**
     * Returns the lore lines for this attribute as Adventure text components.
     *
     * @param container the attribute container providing context for generating lore lines
     * @return a non-null list of {@link Component} representing the lore lines; returns an empty list if no lore is present
     */
    @NotNull List<Component> loreLines(@NotNull AttributeContainer container);

    /**
     * Returns the sorting priority of this attribute's lore lines relative to other lore attributes.
     * <p>
     * Lore lines with lower priority values appear earlier in the item's tooltip.
     *
     * @return the priority value for sorting lore lines
     */
    int priority();
}