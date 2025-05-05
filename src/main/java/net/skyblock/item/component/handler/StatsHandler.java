package net.skyblock.item.component.handler;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.item.component.definition.StatsComponent;
import net.skyblock.item.component.trait.LoreHandler;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.StatValueType;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.skyblock.utils.Utils.formatStatValue;

/**
 * Handles the stats component for items.
 * This handler is responsible for:
 * - Calculating final stats by combining base stats with modifiers
 * - Generating appropriate lore for displaying item stats
 */
public class StatsHandler implements ItemComponentHandler<StatsComponent>, LoreHandler<StatsComponent> {
    private static final String COMPONENT_ID = "statistics";

    @Override
    public @NotNull Class<StatsComponent> componentType() {
        return StatsComponent.class;
    }

    @Override
    public @NotNull String componentId() {
        return COMPONENT_ID;
    }

    @Override
    public int lorePriority() {
        return 0;
    }

    /**
     * Calculates the final stats for an item by combining base stats with all modifiers.
     *
     * @param component The stats component containing base stats and modifiers
     * @param container The full component container, which may be needed by modifiers
     * @return A new StatProfile containing the combined stats
     */
    public @NotNull StatProfile getFinalStats(@NotNull StatsComponent component, @NotNull ComponentContainer container) {
        StatProfile result = component.baseStats().copy();

        for (ModifierComponent modifier : component.modifiers()) {
            result.combineWith(modifier.getModifierHandler().getStatProfile(modifier, container));
        }

        return result;
    }

    /**
     * Generates lore lines for this component to be displayed on the item.
     *
     * @param component The stats component to generate lore for
     * @param container The full component container, in case lore depends on other components
     * @return List of Components representing lore lines
     */
    @Override
    public @NotNull List<Component> generateLore(@NotNull StatsComponent component, @NotNull ComponentContainer container) {
        StatProfile profile = component.getFinalStats(container);
        ObjectArrayList<ModifierComponent> modifiers = component.modifiers();
        List<Component> lore = new ArrayList<>();

        for (Statistic stat : Statistic.getValues()) {
            double value = profile.get(stat);
            if (value > 0) {
                lore.add(formatStatLine(stat, value, modifiers, container));
            }
        }

        return lore;
    }

    /**
     * Creates a component instance from JSON data
     *
     * @param json The JSON data to parse
     * @return The created component instance
     */
    @Override
    public @NotNull StatsComponent fromJson(@NotNull JsonElement json) {
        StatProfile profile = new StatProfile(false);

        if (json.isJsonObject()) {
            json.getAsJsonObject().entrySet().forEach(entry -> {
                String name = entry.getKey();
                try {
                    profile.addStat(
                            Statistic.valueOf(name),
                            StatValueType.BASE,
                            entry.getValue().getAsDouble()
                    );
                } catch (IllegalArgumentException _) {
                    Logger.warn("Invalid stat: {}", name);
                }
            });
        }

        return new StatsComponent(profile);
    }

    /**
     * Creates a formatted text component for a stat line with modifiers
     *
     * @param stat The statistic being formatted
     * @param finalValue The final calculated value of the stat
     * @param modifiers List of modifiers affecting the stat
     * @param container The full component container
     * @return A formatted Component for display in lore
     */
    private Component formatStatLine(@NotNull Statistic stat, double finalValue, @NotNull ObjectArrayList<ModifierComponent> modifiers, @NotNull ComponentContainer container) {
        String formattedValue = formatStatValue(finalValue, stat);

        Component line = textOfChildren(
                text(stat.getDisplayName() + ": ", GRAY),
                text(formattedValue, stat.getLoreColor()));

        for (ModifierComponent modifier : modifiers) {
            var handler = modifier.getModifierHandler();
            double modifierValue = handler.getStatProfile(modifier, container).get(stat);

            if (modifierValue == 0) continue;

            Component modifierText = handler.formatStatDisplay(stat, modifierValue);
            line = line.append(text(" ")).append(modifierText);
        }

        return line.decoration(ITALIC, false);
    }
}