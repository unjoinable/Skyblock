package net.skyblock.item.component.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ModifierComponent;
import net.skyblock.item.component.handlers.StatsHandler;
import net.skyblock.stats.StatProfile;
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
     * Adds a new stat modifier to this component.
     *
     * @param modifier The modifier to add
     * @return This StatsComponent instance for method chaining
     */
    public StatsComponent addModifier(ModifierComponent modifier) {
        modifiers.add(modifier);
        return this;
    }

    /**
     * Removes a stat modifier from this component.
     *
     * @param modifier The modifier to remove
     * @return True if the modifier was found and removed, false otherwise
     */
    public boolean removeModifier(ModifierComponent modifier) {
        return modifiers.remove(modifier);
    }

    public @NotNull StatProfile getFinalStats(@NotNull ComponentContainer container) {
        StatsHandler handler = (StatsHandler) getHandler();
        assert handler != null;
        return handler.getFinalStats(this, container);
    }
}