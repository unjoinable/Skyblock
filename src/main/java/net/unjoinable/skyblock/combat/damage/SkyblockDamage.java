package net.unjoinable.skyblock.combat.damage;

import net.minestom.server.entity.Entity;
import org.jspecify.annotations.Nullable;

/**
 * Represents a damage event in the Skyblock combat system.
 *
 * @param rawDamage The raw damage amount before any modifiers
 * @param damageType The type of damage being dealt
 * @param damager The entity causing the damage, null if caused by server/environment
 * @param target The entity receiving the damage
 */
public record SkyblockDamage(
        double rawDamage,
        DamageType damageType,
        @Nullable Entity damager,
        Entity target) {

    /**
     * Creates a new builder instance for constructing SkyblockDamage objects.
     *
     * @return A new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for creating SkyblockDamage instances with default values and validation.
     */
    public static class Builder {
        private double rawDamage = 0.0;
        private DamageType damageType = DamageType.SERVER;
        private @Nullable Entity damager = null;
        private @Nullable Entity target = null;

        private Builder() {}

        /**
         * Sets the raw damage amount.
         *
         * @param rawDamage The damage amount
         * @return This builder instance for method chaining
         */
        public Builder rawDamage(double rawDamage) {
            this.rawDamage = rawDamage;
            return this;
        }

        /**
         * Sets the damage type.
         *
         * @param damageType The type of damage
         * @return This builder instance for method chaining
         */
        public Builder damageType(DamageType damageType) {
            this.damageType = damageType;
            return this;
        }

        /**
         * Sets the entity causing the damage.
         *
         * @param damager The damaging entity (can be null for environmental damage)
         * @return This builder instance for method chaining
         */
        public Builder damager(@Nullable Entity damager) {
            this.damager = damager;
            return this;
        }

        /**
         * Sets the target entity receiving the damage.
         *
         * @param target The target entity (required)
         * @return This builder instance for method chaining
         */
        public Builder target(Entity target) {
            this.target = target;
            return this;
        }

        /**
         * Builds the SkyblockDamage instance.
         *
         * @return A new SkyblockDamage instance
         * @throws IllegalStateException if target is null
         */
        public SkyblockDamage build() {
            if (target == null) {
                throw new IllegalStateException("Target entity cannot be null");
            }

            return new SkyblockDamage(rawDamage, damageType, damager, target);
        }
    }
}