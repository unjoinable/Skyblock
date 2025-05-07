package net.skyblock.item.component.trait;

import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.ItemComponents;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for item components that modify player or entity statistics.
 * Provides functionality to retrieve stat modifications and format them for display.
 *
 * @param <C> The type of item component this handler manages
 */
public interface ModifierHandler<C extends ItemComponent> extends ItemComponentHandler<C> {

    /**
     * Gets the stat profile containing all statistic modifications provided by this component.
     *
     * @param component The component to get data from
     * @param container The container holding the item component
     * @return A stat profile with all applicable statistic modifications
     */
    @SuppressWarnings("unchecked")
    default @NotNull StatProfile getStatProfile(@NotNull ModifierComponent component, @NotNull ItemComponents container) {
        return this.getStatProfile(((C) component), container);
    }

    /**
     * Gets the stat profile containing all statistic modifications provided by this component.
     *
     * @param component The component to get data from
     * @param container The container holding the item component
     * @return A stat profile with all applicable statistic modifications
     */
    @NotNull StatProfile getStatProfile(@NotNull C component, @NotNull ItemComponents container);

    /**
     * Formats a statistic and its value into a displayable text component.
     * Used for rendering the stat in tooltips, GUIs, or other displays.
     *
     * @param stat The statistic to format
     * @param value The value of the statistic
     * @return A formatted text component for display
     */
    @NotNull Component formatStatDisplay(@NotNull Statistic stat, double value);
}