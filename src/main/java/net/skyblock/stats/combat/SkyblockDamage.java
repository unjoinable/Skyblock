package net.skyblock.stats.combat;

/**
 * Represents a damage event within the Skyblock system.
 * Contains information about damage amount, source, target, and other properties.
 * Implemented as an immutable record.
 */
public record SkyblockDamage(
        boolean isProjectile,
        boolean isMagicDamage,
        double damage,
        boolean isCriticalDamage,
        DamageReason reason,
        CombatEntity source,
        CombatEntity target
) {
    /**
     * Creates a builder for constructing SkyblockDamage instances
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for creating SkyblockDamage instances with a fluent API
     */
    public static class Builder {
        private boolean isProjectile = false;
        private boolean isMagicDamage = false;
        private double damage = 0;
        private boolean isCriticalDamage = false;
        private DamageReason reason = DamageReason.OTHER;
        private CombatEntity source = null;
        private CombatEntity target = null;

        /**
         * Sets whether the damage is from a projectile
         *
         * @param isProjectile true if projectile damage, false otherwise
         * @return this builder for chaining
         */
        public Builder projectile(boolean isProjectile) {
            this.isProjectile = isProjectile;
            return this;
        }

        /**
         * Sets whether the damage is magical in nature
         *
         * @param isMagicDamage true if magical damage, false if physical
         * @return this builder for chaining
         */
        public Builder magic(boolean isMagicDamage) {
            this.isMagicDamage = isMagicDamage;
            return this;
        }

        /**
         * Sets the damage amount
         *
         * @param damage the raw damage amount
         * @return this builder for chaining
         */
        public Builder damage(double damage) {
            this.damage = damage;
            return this;
        }

        /**
         * Sets whether the damage is a critical hit
         *
         * @param isCriticalDamage true if critical hit, false otherwise
         * @return this builder for chaining
         */
        public Builder critical(boolean isCriticalDamage) {
            this.isCriticalDamage = isCriticalDamage;
            return this;
        }

        /**
         * Sets the reason for the damage
         *
         * @param reason the damage reason
         * @return this builder for chaining
         */
        public Builder reason(DamageReason reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Sets the source entity
         *
         * @param source the entity causing the damage
         * @return this builder for chaining
         */
        public Builder source(CombatEntity source) {
            this.source = source;
            return this;
        }

        /**
         * Sets the target entity
         *
         * @param target the entity receiving the damage
         * @return this builder for chaining
         */
        public Builder target(CombatEntity target) {
            this.target = target;
            return this;
        }

        /**
         * Builds a new SkyblockDamage instance with the configured values
         *
         * @return the new SkyblockDamage instance
         * @throws IllegalStateException if source or target are null
         */
        public SkyblockDamage build() {
            if (source == null || target == null) {
                throw new IllegalStateException("Source and target must be specified");
            }
            return new SkyblockDamage(isProjectile, isMagicDamage, damage, isCriticalDamage, reason, source, target);
        }
    }
}
