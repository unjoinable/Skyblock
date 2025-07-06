package net.unjoinable.skyblock.combat.damage;

import net.minestom.server.entity.Entity;
import org.jspecify.annotations.Nullable;

/**
 * Represents a damage event in the Skyblock combat system.
 *
 * @param rawDamage The raw damage amount before any modifiers
 * @param damageType The type of damage being dealt
 * @param damageReason The reason or source of the damage
 * @param isCritical Whether this damage is a critical hit
 * @param damager The entity causing the damage, null if caused by server/environment
 * @param target The entity receiving the damage
 */
public record SkyblockDamage(
        double rawDamage,
        DamageType damageType,
        DamageReason damageReason,
        boolean isCritical,
        @Nullable Entity damager,
        Entity target) {

    /**
     * Creates a new SkyblockDamage instance with a different raw damage amount.
     *
     * @param rawDamage The new raw damage amount
     * @return A new SkyblockDamage instance with the updated raw damage
     */
    public SkyblockDamage withRawDamage(double rawDamage) {
        return new SkyblockDamage(rawDamage, damageType, damageReason, isCritical, damager, target);
    }

    /**
     * Creates a new SkyblockDamage instance with a different damage type.
     *
     * @param damageType The new damage type
     * @return A new SkyblockDamage instance with the updated damage type
     */
    public SkyblockDamage withDamageType(DamageType damageType) {
        return new SkyblockDamage(rawDamage, damageType, damageReason, isCritical, damager, target);
    }

    /**
     * Creates a new SkyblockDamage instance with a different damage reason.
     *
     * @param reason The new damage reason to apply
     * @return A new SkyblockDamage instance with the updated reason
     */
    public SkyblockDamage withReason(DamageReason reason) {
        return new SkyblockDamage(rawDamage, damageType, reason, isCritical, damager, target);
    }

    /**
     * Creates a new SkyblockDamage instance with a different critical status.
     *
     * @param isCritical Whether this damage is critical
     * @return A new SkyblockDamage instance with the updated critical status
     */
    public SkyblockDamage withCritical(boolean isCritical) {
        return new SkyblockDamage(rawDamage, damageType, damageReason, isCritical, damager, target);
    }

    /**
     * Creates a new SkyblockDamage instance with a different damager entity.
     *
     * @param damager The new damager entity (can be null)
     * @return A new SkyblockDamage instance with the updated damager
     */
    public SkyblockDamage withDamager(@Nullable Entity damager) {
        return new SkyblockDamage(rawDamage, damageType, damageReason, isCritical, damager, target);
    }

    /**
     * Creates a new SkyblockDamage instance with a different target entity.
     *
     * @param target The new target entity
     * @return A new SkyblockDamage instance with the updated target
     */
    public SkyblockDamage withTarget(Entity target) {
        return new SkyblockDamage(rawDamage, damageType, damageReason, isCritical, damager, target);
    }

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
        private DamageType damageType = DamageType.UNKNOWN;
        private DamageReason damageReason = DamageReason.SERVER;
        private boolean isCritical = false;
        private @Nullable Entity damager;
        private @Nullable Entity target;

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
         * Sets the damage reason.
         *
         * @param damageReason The reason for the damage
         * @return This builder instance for method chaining
         */
        public Builder damageReason(DamageReason damageReason) {
            this.damageReason = damageReason;
            return this;
        }

        /**
         * Sets whether this damage is critical.
         *
         * @param isCritical True if this is a critical hit
         * @return This builder instance for method chaining
         */
        public Builder isCritical(boolean isCritical) {
            this.isCritical = isCritical;
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
                throw new IllegalStateException("Target entity is required but was null");
            }

            return new SkyblockDamage(rawDamage, damageType, damageReason, isCritical, damager, target);
        }
    }
}