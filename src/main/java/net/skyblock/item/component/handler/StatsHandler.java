package net.skyblock.item.component.handler;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ItemComponents;
import net.skyblock.item.component.ItemComponentHandler;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.item.component.definition.StatsComponent;
import net.skyblock.item.component.trait.LoreHandler;
import net.skyblock.item.component.trait.ModifierHandler;
import net.skyblock.registry.impl.HandlerRegistry;
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
 * <p>
 * This handler calculates final statistics by combining base stats with modifiers
 * and generates appropriate lore for displaying item statistics in the UI.
 */
public class StatsHandler implements ItemComponentHandler<StatsComponent>, LoreHandler<StatsComponent> {

    private static final String COMPONENT_ID = "statistics";
    private final HandlerRegistry handlers;

    /**
     * Constructs a new StatsHandler with the specified handler registry.
     *
     * @param handlers the registry containing available component handlers
     * @throws NullPointerException if handlers is null
     */
    public StatsHandler(@NotNull HandlerRegistry handlers) {
        this.handlers = handlers;
    }

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
     * @param component the stats component containing base stats
     * @param container the component container which may be needed by modifiers
     * @return a new StatProfile containing the combined stats
     */
    public @NotNull StatProfile getFinalStats(@NotNull StatsComponent component, @NotNull ItemComponents container) {
        StatProfile result = component.baseStats().copy();

        for (ModifierComponent modifier : component.modifiers()) {
            ModifierHandler<?> handler = (ModifierHandler<?>) handlers.getHandler(modifier.getType()).get();
            result.combineWith(handler.getStatProfile(modifier, container));
        }

        return result;
    }

    /**
     * Generates lore lines for this component to be displayed on the item.
     *
     * @param component the stats component to generate lore for
     * @param container the component container, in case lore depends on other components
     * @return list of Components representing lore lines
     */
    @Override
    public @NotNull List<Component> generateLore(@NotNull StatsComponent component, @NotNull ItemComponents container) {
        StatProfile profile = getFinalStats(component, container);
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
     * Creates a component instance from JSON data.
     *
     * @param json the JSON data to parse
     * @return the created StatsComponent instance
     */
    @Override
    public @NotNull StatsComponent fromJson(@NotNull JsonElement json) {
        StatProfile profile = new StatProfile(false);

        if (json.isJsonObject()) {
            json.getAsJsonObject().entrySet().forEach(entry -> {
                String statName = entry.getKey();
                try {
                    Statistic stat = Statistic.valueOf(statName);
                    double value = entry.getValue().getAsDouble();
                    profile.addStat(stat, StatValueType.BASE, value);
                } catch (IllegalArgumentException _) {
                    Logger.warn("Invalid stat: {}", statName);
                }
            });
        }

        return new StatsComponent(profile);
    }

    /**
     * Creates a formatted text component for a stat line with modifiers.
     *
     * @param stat the statistic being formatted
     * @param finalValue the final calculated value of the stat
     * @param modifiers list of modifiers affecting the stat
     * @param container the full component container
     * @return a formatted Component for display in lore
     */
    private Component formatStatLine(
            @NotNull Statistic stat,
            double finalValue,
            @NotNull ObjectArrayList<ModifierComponent> modifiers,
            @NotNull ItemComponents container) {

        String formattedValue = formatStatValue(finalValue, stat);

        Component line = textOfChildren(
                text(stat.getDisplayName() + ": ", GRAY),
                text(formattedValue, stat.getLoreColor()));

        for (ModifierComponent modifier : modifiers) {
            ModifierHandler<?> handler = (ModifierHandler<?>) handlers.getHandler(modifier.getType()).get();
            double modifierValue = handler.getStatProfile(modifier, container).get(stat);

            if (modifierValue == 0) {
                continue;
            }

            Component modifierText = handler.formatStatDisplay(stat, modifierValue);
            line = line.append(text(" ")).append(modifierText);
        }

        return line.decoration(ITALIC, false);
    }
}