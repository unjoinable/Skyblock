package net.skyblock.item.handlers;

import net.kyori.adventure.text.Component;
import net.skyblock.item.ItemComponentHandler;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Combined interface for components that contribute to item lore.
 * Implements Comparable to allow automatic sorting based on lore priority.
 */
public interface LoreHandler<C extends ItemComponent> extends ItemComponentHandler<C>, Comparable<LoreHandler<?>> {

    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     *
     * @return the priority value for sorting
     */
    int lorePriority();

    /**
     * Generates lore lines for this component.
     *
     * @param component the component to generate lore for
     * @param container the full component container, in case this lore depends on other components
     * @return list of components representing lore lines
     */
    @NotNull List<Component> generateLore(@NotNull C component, @NotNull ComponentContainer container);

    /**
     * Compares this handler with another based on their lore priorities.
     * Allows LoreHandlers to be naturally sorted by priority.
     *
     * @param other the other LoreHandler to compare with
     * @return a negative value if this handler should appear before the other,
     *         a positive value if this handler should appear after the other,
     *         or zero if they have the same priority
     */
    @Override
    default int compareTo(@NotNull LoreHandler<?> other) {
        return Integer.compare(this.lorePriority(), other.lorePriority());
    }
}