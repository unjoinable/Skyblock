package net.unjoinable.skyblock.combat.damage;

/**
 * Represents the source or cause of damage in the Skyblock combat system.
 */
public enum DamageReason {
    SERVER,
    PLAYER,
    ENTITY,
    FALL,
    ABILITY,
    ENVIRONMENT, // Drowning / Fire
    FEROCITY,
}
