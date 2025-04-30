package net.skyblock.item.component.trait;

import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface for components that generate lore text.
 */
public interface LoreProvider extends ItemComponent {

    /**
     * Generates lore lines for this component.
     * @param container the full component container, in case this lore depends on other components
     * @return list of components representing lore lines
     */
    @NotNull List<Component> generateLore(@NotNull ComponentContainer container);
}
