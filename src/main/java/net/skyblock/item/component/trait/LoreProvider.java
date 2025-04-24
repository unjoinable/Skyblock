package net.skyblock.item.component.trait;

import net.skyblock.item.component.Component;
import net.skyblock.item.component.ComponentContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface for components that generate lore text.
 */
public interface LoreProvider extends Component {

    /**
     * Generates lore lines for this component.
     * @param container the full component container, in case this lore depends on other components
     * @return list of components representing lore lines
     */
    @NotNull List<net.kyori.adventure.text.Component> generateLore(@NotNull ComponentContainer container);
}
