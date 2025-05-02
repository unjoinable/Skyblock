package net.skyblock.item.component.handlers;

import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.handlers.trait.ModifierHandler;
import net.skyblock.item.component.impl.ReforgeComponent;
import net.skyblock.item.component.service.ComponentResolver;
import net.skyblock.item.enums.Rarity;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;
import net.skyblock.utils.Utils;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.skyblock.utils.Utils.formatStatValue;

public class ReforgeHandler implements ModifierHandler<ReforgeComponent> {
    private static final String ID = "reforge";

    /**
     * Gets the stat profile containing all statistic modifications provided by this component.
     *
     * @param component The component to get data from
     * @param container The container holding the item component
     * @return A stat profile with all applicable statistic modifications
     */
    @Override
    public @NotNull StatProfile getStatProfile(@NotNull ReforgeComponent component, @NotNull ComponentContainer container) {
        ComponentResolver resolver = new ComponentResolver();
        Rarity rarity = resolver.resolveRarity(container);

        if (component.hasReforge()) {
            return component.reforge().getStats(rarity);
        }
        return new StatProfile();
    }

    /**
     * Formats a statistic and its value into a displayable text component.
     * Used for rendering the stat in tooltips, GUIs, or other displays.
     *
     * @param stat  The statistic to format
     * @param value The value of the statistic
     * @return A formatted text component for display
     */
    @Override
    public @NotNull Component formatStatDisplay(@NotNull Statistic stat, double value) {
        return Component.text("(" + formatStatValue(value, stat) +")", BLUE);
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<ReforgeComponent> componentType() {
        return ReforgeComponent.class;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }
}
