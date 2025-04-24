package net.skyblock.stats.combat;

import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;

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
     * Performs an attack on the target entity with the specified damage reason
     *
     * @param target the entity to attack
     * @param reason the reason for the damage (e.g., MELEE, PROJECTILE, MAGIC)
     */
    void attack(CombatEntity target, DamageReason reason);

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
     * @param isCritical whether the damage was a critical hit
     */
    void spawnDamageIndicator(double damage, boolean isCritical);

    /**
     * Convenience method to perform a melee attack on a target
     *
     * @param target the entity to attack
     */
    default void meleeDamage(CombatEntity target) {
        attack(target, DamageReason.MELEE);
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
     * Calculates the absolute damage that would be dealt by this entity
     * based on its base damage and strength stats
     *
     * @return the calculated damage value
     */
    default double calculateAbsoluteDamage() {
        StatProfile stats = getStatProfile();
        double baseDamage = stats.get(Statistic.DAMAGE);
        double strength = stats.get(Statistic.STRENGTH);

        return (5 + baseDamage) * (1 + strength / 100);
    }

    /**
     * Calculates the damage reduction from defense
     *
     * @param damage the incoming damage amount
     * @return the amount of damage after defense reduction
     */
    default double applyDefenseReduction(double damage) {
        double defense = getStatProfile().get(Statistic.DEFENSE);
        return damage * (1 - (defense / (defense + 100)));
    }

    /**
     * Kills this entity
     */
    void kill();
}
