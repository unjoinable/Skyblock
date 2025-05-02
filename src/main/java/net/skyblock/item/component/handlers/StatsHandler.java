package net.skyblock.item.component.handlers;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.item.component.handlers.trait.LoreHandler;
import net.skyblock.item.component.impl.StatsComponent;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.StatValueType;
import net.skyblock.stats.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * Handles the stats component for items.
 * This handler is responsible for:
 * - Calculating final stats by combining base stats with modifiers
 * - Generating appropriate lore for displaying item stats
 */
public class StatsHandler implements ItemComponentHandler<StatsComponent>, LoreHandler<StatsComponent> {
    private static final String ID = "statistics";

    /**
     * Returns the component type this handler is responsible for.
     *
     * @return The StatsComponent class
     */
    @Override
    public @NotNull Class<StatsComponent> componentType() {
        return StatsComponent.class;
    }

    /**
     * Calculates the final stats for an item by combining base stats with all modifiers.
     *
     * @param component The stats component containing base stats and modifiers
     * @param container The full component container, which may be needed by modifiers
     * @return A new StatProfile containing the combined stats
     */
    public @NotNull StatProfile getFinalStats(
            @NotNull StatsComponent component,
            @NotNull ComponentContainer container) {
        StatProfile result = component.baseStats().copy();

        for (ModifierComponent modifier : component.modifiers()) {
            result.combineWith(modifier.getModifierHandler().getStatProfile(modifier, container));
        }

        return result;
    }

    /**
     * Returns the unique identifier for this component type.
     *
     * @return The component ID
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }

    /**
     * Determines the lore position priority.
     * Lower values appear earlier in the item lore.
     *
     * @return The priority value for sorting
     */
    @Override
    public int lorePriority() {
        return 0;
    }

    /**
     * Generates lore lines for this component to be displayed on the item.
     * Currently returns an empty list, but can be extended to show stats information.
     *
     * @param component The stats component to generate lore for
     * @param container The full component container, in case lore depends on other components
     * @return List of Components representing lore lines
     */
    @Override
    public @NotNull List<Component> generateLore(@NotNull StatsComponent component, @NotNull ComponentContainer container) {
        var profile = component.getFinalStats(container);
        var modifiers = component.modifiers();
        var lore = new ArrayList<Component>();

        for (Statistic stat : Statistic.getValues()) {
            double value = profile.get(stat);
            if (value > 0) {
                lore.add(formatStatLine(stat, value, modifiers));
            }
        }
        return lore;
    }

    /**
     * Creates a component instance from JSON data
     *
     * @param json The JSON data to parse
     * @return The created component instance
     * @throws UnsupportedOperationException by default unless overridden
     */
    @Override
    public StatsComponent fromJson(@NotNull JsonElement json) {
        StatProfile profile = new StatProfile(false);
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            String name = entry.getKey();
            try {
                Statistic stat = Statistic.valueOf(name);
                double value = entry.getValue().getAsDouble();
                profile.addStat(stat, StatValueType.BASE, value);
            } catch (IllegalArgumentException _) {} //ignored exception
        }

        return new StatsComponent(profile);
    }

    /**
     * Creates a formatted text component for a stat line with modifiers
     */
    private Component formatStatLine(Statistic stat, double value, ObjectArrayList<ModifierComponent> modifiers) {
        boolean isPercent = stat.getPercentage();
        String format = isPercent ? "%.1f%%" : "%.0f";
        String formattedValue = String.format(format, value);

        var line = textOfChildren(
                text(stat.getDisplayName() + ": ", GRAY),
                text("+" + formattedValue + (isPercent ? "%" : ""), stat.getLoreColor()));

        for (ModifierComponent modifier : modifiers) {
            var handler = modifier.getModifierHandler();
            var text = handler.formatStatDisplay(stat, value);
            line = line.append(text(" ")).append(text);
        }

        return line.decoration(ITALIC, false);
    }

}