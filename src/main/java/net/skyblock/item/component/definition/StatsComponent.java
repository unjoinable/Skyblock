package net.skyblock.item.component.definition;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.stats.calculator.StatProfile;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a component for managing stats on an item.
 * <p>
 * This component holds the base stats of an item as well as a collection of stat modifiers
 * that can alter the final stat values. It serves as the central mechanism for stat management
 * within the item system.
 * </p>
 *
 * @param baseStats The profile containing the base stat values
 * @param modifiers Components that can modify stats based on various conditions
 */
public record StatsComponent(
        @NotNull StatProfile baseStats,
        @NotNull ObjectArrayList<ModifierComponent> modifiers) implements ItemComponent {

    /**
     * Creates a new StatsComponent with the specified base stats and an empty list of modifiers.
     *
     * @param baseStats The base stats for this component
     */
    public StatsComponent(@NotNull StatProfile baseStats) {
        this(baseStats, new ObjectArrayList<>());
    }

    /**
     * Creates a new StatsComponent with default stats and no modifiers.
     * Uses a non-reusable StatProfile as the base stats.
     */
    public StatsComponent() {
        this(new StatProfile(false));
    }

    /**
     * Creates a new StatsComponent with an additional modifier.
     *
     * @param modifier The modifier to add
     * @return A new StatsComponent with the added modifier
     */
    public StatsComponent withModifier(ModifierComponent modifier) {
        ObjectArrayList<ModifierComponent> newModifiers = new ObjectArrayList<>(this.modifiers);
        newModifiers.add(modifier);
        return new StatsComponent(this.baseStats, newModifiers);
    }

    /**
     * Creates a new StatsComponent without the specified modifier.
     *
     * @param modifier The modifier to remove
     * @return A new StatsComponent without the specified modifier
     */
    public StatsComponent withoutModifier(ModifierComponent modifier) {
        ObjectArrayList<ModifierComponent> newModifiers = new ObjectArrayList<>(this.modifiers);
        newModifiers.remove(modifier);
        return new StatsComponent(this.baseStats, newModifiers);
    }
}