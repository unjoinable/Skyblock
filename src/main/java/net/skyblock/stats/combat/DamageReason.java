package net.skyblock.stats.combat;

/**
 * Represents the various ways an entity can take damage in the Skyblock system.
 * Used to determine damage calculations and effects.
 */
public enum DamageReason {
    /**
     * Damage from close-range weapons or attacks
     */
    MELEE,

    /**
     * Damage from ranged projectiles like arrows
     */
    PROJECTILE,

    /**
     * Damage from magic spells and abilities
     */
    MAGIC,

    /**
     * Damage from falling
     */
    FALL,

    /**
     * Damage from fire or lava
     */
    FIRE,

    /**
     * Damage from drowning in water
     */
    DROWNING,

    /**
     * Damage from explosions
     */
    EXPLOSION,

    /**
     * Damage from void (falling out of the world)
     */
    VOID,

    /**
     * Damage that bypasses armor and resistance
     */
    TRUE_DAMAGE,

    /**
     * Damage from environmental effects like poison
     */
    STATUS_EFFECT,

    /**
     * Miscellaneous damage that doesn't fit other categories
     */
    OTHER
}
