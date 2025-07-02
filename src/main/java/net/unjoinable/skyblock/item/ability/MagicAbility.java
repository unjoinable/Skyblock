package net.unjoinable.skyblock.item.ability;

/**
 * An item ability that deals magic-based damage with scaling properties.
 *
 * <p>This interface extends ItemAbility to provide magic damage calculations
 * based on a base damage value and a scaling factor. Magic abilities typically
 * scale with player stats like intelligence or magic power.
 */
public interface MagicAbility extends ItemAbility {

    /**
     * Returns the base damage value for this magic ability.
     *
     * <p>This is the minimum damage dealt before any scaling calculations
     * are applied. The actual damage will be this value multiplied by
     * the scaling factor and potentially modified by player stats.
     *
     * @return the base damage amount
     */
    int baseAbilityDamage();

    /**
     * Returns the scaling multiplier for this magic ability.
     *
     * <p>This value determines how much the base damage is amplified
     * by player stats or other factors. A scaling of 1.0 means no
     * amplification, while higher values increase the damage scaling.
     *
     * @return the scaling multiplier
     */
    double abilityScalling();

}
