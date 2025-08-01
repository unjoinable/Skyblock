package net.unjoinable.skyblock.item.ability;

import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.minestom.server.codec.Codec;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.registry.registries.AbilityRegistry;
import net.unjoinable.skyblock.utils.codec.ItemAbilityCodec;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Represents a special ability attached to a SkyBlock item.
 * Abilities have a unique identifier, trigger type, optional resource cost,
 * description lines, and executable logic.
 */
public interface ItemAbility extends Keyed {
    Codec<ItemAbility> CODEC = new ItemAbilityCodec(AbilityRegistry.withDefaults());

    /**
     * Returns the display name of the ability, typically shown in user interfaces
     * such as item tooltips or ability menus. This is a human-readable name
     * that represents the ability's identity and purpose.
     *
     * @return the ability's display name as a {@link String}
     */
    String displayName();

    /**
     * Returns how the ability is triggered (e.g., right-click, sneak).
     *
     * @return the execution trigger type
     */
    ExecutionType trigger();

    /**
     * Returns the type of resource cost required to activate the ability.
     *
     * @return the cost type (e.g., MANA, COINS, or FREE)
     */
    AbilityCostType costType();

    /**
     * Returns the numeric cost value required to activate the ability.
     *
     * @return the cost amount, or 0 for free abilities
     */
    int cost();

    /**
     * Returns the delay (in milliseconds) before this ability can be used again.
     *
     * @return cooldown duration in milliseconds
     */
    long cooldown();

    /**
     * Returns the lines shown to describe this ability in tooltips.
     *
     * @return a list of description components
     */
    List<Component> description();

    /**
     * Returns the logic executed when the ability is triggered.
     *
     * @return a consumer handling the ability's effect
     */
    BiConsumer<SkyblockPlayer, SkyblockItem> action();
}