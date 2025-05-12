package net.skyblock.stats.definition;

/**
 * Contains commonly used damage attributes as constants.
 * This interface serves as a centralized location for all game damage modifiers.
 * <p></p>
 * Classes can implement this interface to gain direct access to these constants,
 * or reference them statically via DamageAttributes.ATTRIBUTE_NAME.
 */
public interface DamageAttributes {

    /**
     * Indicates if the damage is a critical hit.
     * Critical hits typically deal increased damage based on the attacker's stats.
     */
    DamageAttribute<Boolean> CRITICAL = DamageAttribute.of(false, "CRITICAL");

    /**
     * The ferocity value applied to the damage.
     * Ferocity determines how many additional hits may occur.
     */
    DamageAttribute<Integer> FEROCITY = DamageAttribute.of(0, "FEROCITY");

    /**
     * Indicates if the damage is physical.
     * Physical damage can be reduced by armor and defense stats.
     */
    DamageAttribute<Boolean> PHYSICAL = DamageAttribute.of(false, "PHYSICAL");

    /**
     * Indicates if the damage is magical.
     * Magical damage can be reduced by magic resistance.
     */
    DamageAttribute<Boolean> MAGICAL = DamageAttribute.of(false, "MAGICAL");

    /**
     * Indicates if the damage is true damage.
     * True damage bypasses normal defense calculations.
     */
    DamageAttribute<Boolean> TRUE = DamageAttribute.of(false, "TRUE");

    /**
     * Indicates if the damage is from ranged attacks.
     * May have special interactions with distance-based mechanics.
     */
    DamageAttribute<Boolean> RANGED = DamageAttribute.of(false, "RANGED");

    /**
     * Indicates if the damage is from melee attacks.
     * Usually applies at close range.
     */
    DamageAttribute<Boolean> MELEE = DamageAttribute.of(false, "MELEE");

    /**
     * Indicates if the damage is from special abilities.
     * Often with unique properties.
     */
    DamageAttribute<Boolean> ABILITY = DamageAttribute.of(false, "ABILITY");
}