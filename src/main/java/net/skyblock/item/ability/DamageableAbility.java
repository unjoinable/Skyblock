package net.skyblock.item.ability;

import net.skyblock.stats.StatProfile;
import net.skyblock.stats.combat.CombatEntity;
import net.skyblock.stats.combat.DamageType;
import net.skyblock.stats.combat.SkyblockDamage;

/**
 * Represents an ability that can deal damage.
 * This interface allows for consistent handling of different ability types.
 */
public interface DamageableAbility {
    /**
     * Gets the base damage of this ability
     * @return the base ability damage
     */
    double getBaseAbilityDamage();

    /**
     * Gets the intelligence scaling factor for this ability
     * @return the ability's intelligence scaling
     */
    double getAbilityScaling();

    /**
     * Gets the ability's damage type
     * @return the type of damage this ability deals
     */
    default DamageType getDamageType() {
       return DamageType.ABILITY;
    }

    /**
     * Apply any special effects that should happen when this ability deals damage
     * @param damage The damage instance being applied
     * @param source The source of the damage
     * @param target The target of the damage
     */
    default void onDamageDealt(SkyblockDamage damage, CombatEntity source, CombatEntity target) {
        // Default implementation does nothing
    }

    /**
     * Calculate any additional damage multipliers unique to this ability
     * @param sourceStats The stats of the damage source
     * @param targetStats The stats of the damage target
     * @return The multiplier to apply (1.0 means no change)
     */
    default double calculateDamageMultiplier(StatProfile sourceStats, StatProfile targetStats) {
        return 1.0;
    }
}
