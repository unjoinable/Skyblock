package net.skyblock.item.ability;

import net.kyori.adventure.text.Component;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Represents an ability that can be attached to an item in the Skyblock system.
 * Item abilities provide special functions or effects when activated by players.
 */
public interface Ability {

    /**
     * Gets the unique identifier for this ability.
     *
     * @return A unique string identifier for this ability
     */
    @NotNull String id();

    /**
     * Gets the display name of this ability.
     *
     * @return The name of the ability as shown to players
     */
    @NotNull String name();

    /**
     * Gets the description of this ability.
     * The description typically explains what the ability does and how to use it.
     *
     * @return A list of formatted text components representing the ability description
     */
    @NotNull List<Component> description();

    /**
     * Gets the type of this ability (e.g., RIGHT_CLICK, PASSIVE, etc.).
     *
     * @return The ability type enum value
     */
    @NotNull AbilityType type();

    /**
     * Gets the cooldown duration of this ability in ticks.
     * A value of 0 indicates no cooldown.
     *
     * @return The cooldown period in ticks (20 ticks = 1 second)
     */
    int cooldown();

    /**
     * Executes this ability for the specified player using the given item.
     * This method handles the actual implementation of the ability's effect,
     * including any particle effects, sounds, damage calculations, and other
     * game mechanics associated with the ability.
     *
     */
    @NotNull BiConsumer<SkyblockPlayer, SkyblockItem> executeAbility();

}
