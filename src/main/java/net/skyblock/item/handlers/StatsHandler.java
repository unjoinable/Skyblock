package net.skyblock.item.handlers;

import net.kyori.adventure.text.Component;
import net.skyblock.item.ItemComponentHandler;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.components.StatsComponent;
import net.skyblock.item.handlers.trait.LoreHandler;
import net.skyblock.stats.StatProfile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StatsHandler implements ItemComponentHandler<StatsComponent>, LoreHandler<StatsComponent> {
    private static final String ID = "stats";

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<StatsComponent> componentType() {
        return StatsComponent.class;
    }

    public @NotNull StatProfile getFinalStats(@NotNull StatsComponent component) {
        return null;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }

    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     *
     * @return the priority value for sorting
     */
    @Override
    public int lorePriority() {
        return 0;
    }

    /**
     * Generates lore lines for this component.
     *
     * @param component the component to generate lore for
     * @param container the full component container, in case this lore depends on other components
     * @return list of components representing lore lines
     */
    @Override
    public @NotNull List<Component> generateLore(@NotNull StatsComponent component, @NotNull ComponentContainer container) {
        return List.of();
    }
}
