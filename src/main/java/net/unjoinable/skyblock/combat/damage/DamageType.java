package net.unjoinable.skyblock.combat.damage;

/**
 * Represents the different types of damage that can be dealt in the Skyblock combat system.
 */
public enum DamageType {

    /** Physical damage dealt through close-range combat */
    MELEE,

    /** Damage dealt through projectiles and ranged weapons */
    RANGED,

    /** Damage dealt through magical abilities and spells */
    MAGIC,

    /** Void damage that bypasses most defenses */
    VOID,

    /** Administrative or environmental damage from the server */
    SERVER
}