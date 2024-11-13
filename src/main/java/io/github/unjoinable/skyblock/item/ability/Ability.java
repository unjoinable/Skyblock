package io.github.unjoinable.skyblock.item.ability;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedObject;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an ability that can be used by a {@link SkyblockPlayer} with a {@link SkyblockItem}.
 */
public interface Ability extends NamespacedObject {

    /**
     * Returns the name of the ability.
     *
     * @return the name of the ability
     */
    @NotNull String name();

    /**
     * Returns the cost type of the ability.
     *
     * @return the cost type of the ability
     */
    @NotNull AbilityCostType costType();

    /**
     * Returns the type of the ability.
     *
     * @return the type of the ability
     */
    @NotNull AbilityType type();

    /**
     * Returns the cost of the ability.
     *
     * @return the cost of the ability
     */
    int abilityCost();

    /**
     * Returns the cooldown of the ability in milliseconds.
     *
     * @return the cooldown of the ability in milliseconds
     */
    long cooldownInMs();

    /**
     * Returns the lore of the ability.
     *
     * @return the lore of the ability
     */
    @NotNull List<Component> lore();

    /**
     * Runs the ability for the given {@link SkyblockPlayer} and {@link SkyblockItem}.
     *
     * @param player the player who uses the ability
     * @param item   the item that the ability is used with
     */
    void run(@NotNull SkyblockPlayer player, @NotNull SkyblockItem item);
}

