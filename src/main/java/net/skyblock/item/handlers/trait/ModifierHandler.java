package net.skyblock.item.handlers.trait;

import net.kyori.adventure.text.Component;
import net.skyblock.item.ItemComponentHandler;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;
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
    @NotNull StatProfile getStatProfile(@NotNull C component, @NotNull ComponentContainer container);

    /**
     * Formats a statistic and its value into a displayable text component.
     * Used for rendering the stat in tooltips, GUIs, or other displays.
     *
     * @param stat The statistic to format
     * @param value The value of the statistic
     * @return A formatted text component for display
     */
    @NotNull Component formatStatDisplay(@NotNull Statistic stat, int value);
}