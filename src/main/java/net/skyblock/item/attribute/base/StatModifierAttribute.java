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
     * Returns a profile of all statistic modifications contributed by this attribute, using the provided attribute container for contextual calculations.
     *
     * @param container the attribute container for the current item, used to resolve dependencies or context among attributes
     * @return a StatProfile representing all stat modifications from this attribute
     */
    @NotNull StatProfile getStats(@NotNull AttributeContainer container);

    /**
     * Returns a formatted text component representing the specified statistic and its value for display purposes.
     *
     * @param stat the statistic to display
     * @param value the numeric value of the statistic
     * @return a formatted Component suitable for tooltips or GUIs
     */
    @NotNull Component getFormattedDisplay(@NotNull Statistic stat, double value);
}