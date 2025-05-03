package net.skyblock.stats.holder;

import net.skyblock.stats.calculator.StatProfile;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.skyblock.stats.definition.SkyblockDamage;
import net.skyblock.stats.definition.DamageType;

/**
 * Common interface for all entities that can engage in combat in the Skyblock system.
 * This interface unifies behavior between players and NPCs, allowing for consistent
 * combat mechanics regardless of entity type.
 */
public interface CombatEntity {
    /**
     * Gets the underlying Minestom entity
     *
     * @return the Minestom entity
     */
    Entity getEntity();

    /**
     * Gets the entity's stat profile that tracks and calculates stats
     *
     * @return the entity's stat profile
     */
    StatProfile getStatProfile();

    /**
     * Gets the current health of the entity
     *
     * @return current health value
     */
    double getCurrentHealth();

    /**
     * Sets the current health of the entity
     *
     * @param health the new health value
     */
    void setCurrentHealth(double health);

    /**
     * Gets the maximum possible health of the entity
     *
     * @return maximum health value
     */
    double getMaxHealth();

    /**
     * Gets the current instance the entity is in
     *
     * @return the entity's current instance
     */
    Instance getInstance();

    /**
     * Gets the current position of the entity
     *
     * @return the entity's position
     */
    Pos getPosition();

    /**
     * Checks if the entity is currently invulnerable to damage
     *
     * @return true if invulnerable, false otherwise
     */
    boolean isInvulnerable();

    /**
     * Sets whether the entity is invulnerable to damage
     *
     * @param invulnerable true to make invulnerable, false to make vulnerable
     */
    void setInvulnerable(boolean invulnerable);

    /**
     * Performs an attack on the target entity with the specified damage type
     *
     * @param target the entity to attack
     * @param damageType the type of damage being dealt
     */
    void attack(CombatEntity target, DamageType damageType);

    /**
     * Applies damage to this entity from an incoming damage source
     *
     * @param damage the damage details including source, amount, and reason
     */
    void damage(SkyblockDamage damage);

    /**
     * Applies knockback to this entity from a source entity
     *
     * @param source the entity causing the knockback
     */
    void applyKnockback(Entity source);

    /**
     * Creates and spawns a damage indicator hologram
     *
     * @param damage the amount of damage to display
     */
    void spawnDamageIndicator(SkyblockDamage damage);

    /**
     * Convenience method to perform a melee attack on a target
     *
     * @param target the entity to attack
     */
    default void meleeDamage(CombatEntity target) {
        attack(target, DamageType.MELEE);
    }

    /**
     * Convenience method to determine if the entity is dead
     *
     * @return true if entity's health is 0 or less
     */
    default boolean isDead() {
        return getCurrentHealth() <= 0;
    }


    /**
     * Kills this entity
     */
    void kill();
}