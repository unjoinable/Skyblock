package net.skyblock.item.attribute.base;

import net.kyori.adventure.text.Component;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@link ItemAttribute} that provides numerical modifiers to item stats.
 * Classes implementing this define stat bonuses or penalties (e.g., +Damage, +Speed).
 *
 * <p>Implementations can use the provided AttributeContainer to resolve
 * dependencies or contextual information when calculating stat modifications.</p>
 */
public interface StatModifierAttribute extends ItemAttribute {

    /**
     * Gets the stat profile containing all statistic modifications provided by this attribute.
     * The container parameter provides context about other attributes present on the item,
     * allowing for interdependent calculations.
     *
     * @param container The container holding all attributes for the current item
     * @return A stat profile with all applicable statistic modifications
     */
    @NotNull StatProfile getStats(@NotNull AttributeContainer container);

    /**
     * Formats a statistic and its value into a displayable text component.
     * Used for rendering the stat in tooltips, GUIs, or other displays.
     *
     * @param stat The statistic to format
     * @param value The value of the statistic
     * @return A formatted text component for display
     */
    @NotNull Component getFormattedDisplay(@NotNull Statistic stat, double value);
}