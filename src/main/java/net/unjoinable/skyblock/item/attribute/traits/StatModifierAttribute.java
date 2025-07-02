package net.unjoinable.skyblock.item.attribute.traits;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.SkyblockItem;
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
     * Returns display components for this attribute's stat modifications.
     *
     * <p>This method provides a human-readable representation of the statistical
     * effects this attribute provides, mapped by the specific statistic being modified.
     * The components can be shown in item tooltips, GUIs, or other user interfaces.
     *
     * @return a map of statistics to their display components, never {@code null}
     */
    Map<Statistic, Component> display();

    /**
     * Returns the priority order for applying this stat modifier.
     *
     * <p>When multiple stat modifiers are present on an item or player, they are
     * applied in priority order. Lower values indicate higher priority (applied first).
     * This allows for proper layering of stat effects where order matters.
     *
     * @return the priority value for this modifier, lower values = higher priority
     */
    int modifierPriority();

    /**
     * Determines whether this attribute should be displayed in item tooltips or UI.
     *
     * <p>Some stat modifiers may be internal or hidden from players, while others
     * should be prominently displayed. This method controls the visibility of the
     * attribute in user-facing interfaces.
     *
     * @return {@code true} if this attribute should be displayed to players, {@code false} otherwise
     */
    boolean shouldDisplay();

    /**
     * Determines whether this attribute can be applied to the specified item.
     *
     * <p>This method validates if the attribute is compatible with the given item
     * based on item type, properties, or other constraints. For example, certain
     * stat modifiers might only be applicable to weapons, armor, or specific item categories.
     *
     * @param item the item to check compatibility with
     * @return {@code true} if this attribute can be applied to the item, {@code false} otherwise
     */
    boolean canBeApplied(SkyblockItem item);
}