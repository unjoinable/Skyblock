package net.skyblock.stats.holder;

import net.skyblock.stats.calculator.StatProfile;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.skyblock.stats.definition.Damage;
import org.jetbrains.annotations.NotNull;

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
    @NotNull Entity getEntity();

    /**
     * Gets the entity's stat profile that tracks and calculates stats
     *
     * @return the entity's stat profile
     */
    @NotNull StatProfile getStatProfile();

    /**
     * Gets the current instance the entity is in
     *
     * @return the entity's current instance
     */
    default @NotNull Instance getInstance() {
        return getEntity().getInstance();
    }

    /**
     * Gets the current position of the entity
     *
     * @return the entity's position
     */
     default @NotNull Pos getPosition() {
         return getEntity().getPosition();
     }


    /**
     * Performs an attack on the target entity with the specified damage type
     *
     * @param target the entity to attack
     */
    void attack(CombatEntity target);

    /**
     * Applies damage to this entity from an incoming damage source
     *
     * @param damage the damage details including source, amount, and reason
     */
    void damage(Damage damage);
}