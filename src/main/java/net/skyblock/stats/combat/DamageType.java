package net.skyblock.stats.combat;

/**
 * Defines the various types of damage in the Skyblock system.
 * Used for both classification and specialized handling of different damage sources.
 */
public enum DamageType {
    /**
     * Physical melee damage (sword, axe, etc.)
     */
    MELEE,

    /**
     * Physical ranged damage (bow, crossbow, etc.)
     */
    RANGED,

    /**
     * Magic-based damage (spells, staves, etc.)
     */
    MAGIC,

    /**
     * True damage that bypasses normal defenses
     */
    TRUE,

    /**
     * Fire damage (burning, etc.)
     */
    FIRE,

    /**
     * Environmental damage (fall damage, void, etc.)
     */
    ENVIRONMENTAL,

    /**
     * Damage from poisons or other damage-over-time effects
     */
    POISON,

    /**
     * Special ability damage
     */
    ABILITY,

    /**
     * Uncategorized damage
     */
    OTHER;

    /**
     * Determines if this damage type is considered physical
     * @return true if physical, false otherwise
     */
    public boolean isPhysical() {
        return this == MELEE || this == RANGED;
    }

    /**
     * Determines if this damage type bypasses regular defenses
     * @return true if bypasses defense, false otherwise
     */
    public boolean bypassesDefense() {
        return this == TRUE;
    }

    /**
     * Determines if this damage type can critically hit
     * @return true if can crit, false otherwise
     */
    public boolean canCrit() {
        return this == MELEE || this == RANGED;
    }
}
