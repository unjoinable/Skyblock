package net.unjoinable.skyblock.item.attribute.traits;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.combat.statistic.StatProfile;
import net.unjoinable.skyblock.combat.statistic.Statistic;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * An item attribute that modifies player statistics.
 *
 * <p>This interface defines attributes that can apply statistical modifications
 * to players based on item properties. Implementations should calculate stat
 * bonuses or penalties that get applied when the item is equipped or used.
 *
 * <p>The interface provides both player-specific and generic stat calculation
 * methods to accommodate different use cases where player context may or may
 * not be available.
 */
public interface StatModifierAttribute extends ItemAttribute {

    /**
     * Calculates stat modifications for this attribute without player context.
     *
     * <p>This is a convenience method that delegates to
     * {@link #modifierStats(SkyblockPlayer, AttributeContainer, ItemMetadata)}
     * with a {@code null} player parameter.
     *
     * @param container the attribute container holding the item's attributes
     * @param metadata the item's metadata containing additional information
     * @return a StatProfile containing the calculated stat modifications
     */
    default StatProfile modifierStats(AttributeContainer container, ItemMetadata metadata) {
        return modifierStats(null, container, metadata);
    }

    /**
     * Calculates stat modifications for this attribute with optional player context.
     *
     * <p>Implementations should compute statistical bonuses or penalties based on
     * the attribute's properties, item metadata, and optionally the player's state.
     * The player parameter may be {@code null} when calculating stats without a
     * specific player context (e.g., for item previews or tooltips).
     *
     * <p>When the player is provided, implementations may use player-specific data
     * such as level, equipped items, or other contextual information to calculate
     * more accurate or dynamic stat modifications.
     *
     * @param player the player context for calculations, may be {@code null}
     * @param container the attribute container holding the item's attributes
     * @param metadata the item's metadata containing additional information
     * @return a StatProfile containing the calculated stat modifications, never {@code null}
     */
    StatProfile modifierStats(@Nullable SkyblockPlayer player, AttributeContainer container, ItemMetadata metadata);

    /**
     * Returns a display component for this attribute.
     *
     * <p>This method should return a human-readable representation of the attribute
     * that can be shown in item tooltips, GUIs, or other user interfaces. The
     * component should clearly describe what statistical effect this attribute provides.
     *
     * <p>Implementations should use appropriate formatting, colors, and text styling
     * to make the display informative and visually appealing.
     *
     * @return a Component representing this attribute for display purposes, never {@code null}
     */
    Map<Statistic, Component> display();

    int modifierPriority();

    boolean shouldDisplay();
}