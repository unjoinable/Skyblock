package net.skyblock.item.component.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ItemComponents;
import net.skyblock.item.component.definition.ReforgeComponent;
import net.skyblock.item.component.service.ComponentResolver;
import net.skyblock.item.component.trait.LoreHandler;
import net.skyblock.item.component.trait.ModifierHandler;
import net.skyblock.item.component.trait.NBTHandler;
import net.skyblock.item.enums.Rarity;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.skyblock.utils.Utils.formatStatValue;

public class ReforgeHandler implements ModifierHandler<ReforgeComponent>, LoreHandler<ReforgeComponent>, NBTHandler<ReforgeComponent> {
    private static final String KEY_REFORGE = "reforge";
    private static final String ID = "reforge";

    /**
     * Gets the stat profile containing all statistic modifications provided by this component.
     *
     * @param component The component to get data from
     * @param container The container holding the item component
     * @return A stat profile with all applicable statistic modifications
     */
    @Override
    public @NotNull StatProfile getStatProfile(@NotNull ReforgeComponent component, @NotNull ItemComponents container) {
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
        return text("(" + formatStatValue(value, stat) +")", BLUE);
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

    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     *
     * @return the priority value for sorting
     */
    @Override
    public int lorePriority() {
        return 90;
    }

    /**
     * Generates lore lines for this component.
     *
     * @param component the component to generate lore for
     * @param container the full component container, in case this lore depends on other components
     * @return list of components representing lore lines
     */
    @Override
    public @NotNull List<Component> generateLore(@NotNull ReforgeComponent component, @NotNull ItemComponents container) {
        if (component.hasReforge()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(text("This item can be reforged!", DARK_GRAY).decoration(ITALIC, false));
    }

    /**
     * Deserializes an ItemComponent from NBT data.
     *
     * @param nbt The NBT data containing component information
     * @return An optional new component instance created from the NBT data
     */
    @Override
    public @NotNull Optional<ReforgeComponent> fromNbt(CompoundBinaryTag nbt) {

        return Optional.empty();
    }

    /**
     * Serializes an ItemComponent to NBT data.
     *
     * @param component The component to serialize
     * @return The NBT representation of the component
     */
    @Override
    public CompoundBinaryTag toNbt(@NotNull ReforgeComponent component) {
        return null;
    }
}
