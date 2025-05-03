package net.skyblock.item.ability;

import net.skyblock.stats.definition.DamageType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a magical ability that extends the base ItemAbility interface.
 * Magic abilities consume mana when used and can deal damage based on scaling factors.
 */
public interface MagicAbility extends Ability {

    /**
     * Gets the mana cost required to activate this magic ability.
     *
     * @return The amount of mana points consumed when this ability is used
     */
    int manaCost();

    /**
     * Gets the base damage value of this magic ability before any scaling is applied.
     *
     * @return The base damage value as a double
     */
    double baseAbilityDamage();

    /**
     * Gets the scaling factor for this magic ability's damage.
     * This scaling is typically used with player stats like Intelligence or Ability Damage
     * to calculate the final damage output.
     *
     * @return The scaling factor as a double
     */
    double abilityScaling();

    /**
     * Gets the type of damage this ability deals.
     * The damage type determines how the damage interacts with different armor types,
     * damage reduction effects, and resistances that entities may have.
     *
     * @return The type of damage dealt by this ability
     */
    @NotNull DamageType damageType();
}