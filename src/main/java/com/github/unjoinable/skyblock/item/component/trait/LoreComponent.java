package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;
import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Marker for components that contribute to item lore.
 */
public interface LoreComponent extends Component {

    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     */
    int lorePriority();

    /**
     * Generates lore lines for this component.
     *
     * @param container the full component container, in case this lore depends on other components
     * @return list of text components representing lore lines
     */
    @NotNull List<net.kyori.adventure.text.Component> generateLore(@NotNull ComponentContainer container);
}

