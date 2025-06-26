package net.unjoinable.skyblock.item.attribute.traits;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Represents an {@link ItemAttribute} that provides descriptive text for item lore.
 * Classes implementing this contribute lines to the item's tooltip using rich text components.
 */
public interface LoreAttribute extends ItemAttribute {

    /**
     * Returns the lore lines for this attribute as Adventure text components.
     *
     * @param container the attribute container providing context for generating lore lines
     * @return a non-null list of {@link Component} representing the lore lines; returns an empty list if no lore is present
     */
    default List<Component> loreLines(AttributeContainer container) {
        return loreLines(null, container);
    }

    /**
     * Returns the lore lines for this attribute as Adventure text components.
     *
     * @param player If valid the underlying method should support for both null and non-null player
     * @param container the attribute container providing context for generating lore lines
     * @return a non-null list of {@link Component} representing the lore lines; returns an empty list if no lore is present
     */
    List<Component> loreLines(@Nullable SkyblockPlayer player, AttributeContainer container);

    /**
     * Returns the sorting priority of this attribute's lore lines relative to other lore attributes.
     * <p>
     * Lore lines with lower priority values appear earlier in the item's tooltip.
     *
     * @return the priority value for sorting lore lines
     */
    int priority();
}