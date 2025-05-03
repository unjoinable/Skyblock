package net.skyblock.stats.definition;

import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.ability.MagicAbility;
import net.skyblock.stats.holder.CombatEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a damage event within the Skyblock system.
 * Enhanced with specific damage type support and modular components.
 */
public class SkyblockDamage {
    private final double rawDamage;
    private final boolean isCriticalHit;
    private final DamageType damageType;
    private final CombatEntity sourceEntity;
    private final CombatEntity targetEntity;
    private final int ferocityHits;

    // Optional components that might be present
    private final SkyblockItem weapon;
    private final MagicAbility ability;
    private final Map<Class<?>, Object> attachments;

    private SkyblockDamage(Builder builder) {
        this.rawDamage = builder.rawDamage;
        this.isCriticalHit = builder.isCriticalHit;
        this.damageType = builder.damageType;
        this.sourceEntity = builder.sourceEntity;
        this.targetEntity = builder.targetEntity;
        this.ferocityHits = builder.ferocityHits;
        this.weapon = builder.weapon;
        this.ability = builder.ability;
        this.attachments = new HashMap<>(builder.attachments);
    }

    /**
     * @return The raw calculated damage (before defense reduction)
     */
    public double getRawDamage() {
        return rawDamage;
    }

    /**
     * @return Whether this hit was a critical hit
     */
    public boolean isCriticalHit() {
        return isCriticalHit;
    }

    /**
     * @return The type of damage being dealt
     */
    public DamageType getDamageType() {
        return damageType;
    }


    /**
     * @return The entity that caused this damage
     */
    public CombatEntity getSourceEntity() {
        return sourceEntity;
    }

    /**
     * @return The entity receiving this damage
     */
    public CombatEntity getTargetEntity() {
        return targetEntity;
    }

    /**
     * @return Number of ferocity hits this damage will cause
     */
    public int getFerocityHits() {
        return ferocityHits;
    }

    /**
     * @return The weapon used to cause this damage, if any
     */
    public Optional<SkyblockItem> getWeapon() {
        return Optional.ofNullable(weapon);
    }

    /**
     * @return The ability used to cause this damage, if any
     */
    public Optional<MagicAbility> getAbility() {
        return Optional.ofNullable(ability);
    }

    /**
     * Retrieve a custom attachment by its type
     * @param <T> The expected type of the attachment
     * @param type The class of the attachment
     * @return The attachment or empty if not found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttachment(Class<T> type) {
        return Optional.ofNullable((T) attachments.get(type));
    }

    /**
     * Creates a new builder for constructing a SkyblockDamage instance
     * @return A new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a builder pre-configured with values from this damage instance
     * @return A builder initialized with this instance's values
     */
    public Builder toBuilder() {
        return new Builder()
                .rawDamage(this.rawDamage)
                .criticalHit(this.isCriticalHit)
                .damageType(this.damageType)
                .sourceEntity(this.sourceEntity)
                .targetEntity(this.targetEntity)
                .ferocityHits(this.ferocityHits)
                .weapon(this.weapon)
                .ability(this.ability)
                .withAttachments(this.attachments);
    }


    /**
     * Builder class for creating SkyblockDamage instances with a fluent API
     */
    public static class Builder {
        private double rawDamage = 0;
        private boolean isCriticalHit = false;
        private DamageType damageType = DamageType.MELEE;
        private CombatEntity sourceEntity = null;
        private CombatEntity targetEntity = null;
        private int ferocityHits = 0;
        private SkyblockItem weapon = null;
        private MagicAbility ability = null;
        private final Map<Class<?>, Object> attachments = new HashMap<>();

        /**
         * Sets the raw damage amount (before defense calculations)
         * @param rawDamage The raw damage amount
         * @return this builder for chaining
         */
        public Builder rawDamage(double rawDamage) {
            this.rawDamage = rawDamage;
            return this;
        }


        /**
         * Sets whether the damage is a critical hit
         * @param isCriticalHit true if critical hit, false otherwise
         * @return this builder for chaining
         */
        public Builder criticalHit(boolean isCriticalHit) {
            this.isCriticalHit = isCriticalHit;
            return this;
        }

        /**
         * Sets the damage type
         * @param damageType the type of damage
         * @return this builder for chaining
         */
        public Builder damageType(DamageType damageType) {
            this.damageType = damageType;
            return this;
        }


        /**
         * Sets the source entity
         * @param sourceEntity the entity causing the damage
         * @return this builder for chaining
         */
        public Builder sourceEntity(CombatEntity sourceEntity) {
            this.sourceEntity = sourceEntity;
            return this;
        }

        /**
         * Sets the target entity
         * @param targetEntity the entity receiving the damage
         * @return this builder for chaining
         */
        public Builder targetEntity(CombatEntity targetEntity) {
            this.targetEntity = targetEntity;
            return this;
        }

        /**
         * Sets the number of ferocity hits
         * @param ferocityHits number of additional ferocity hits
         * @return this builder for chaining
         */
        public Builder ferocityHits(int ferocityHits) {
            this.ferocityHits = ferocityHits;
            return this;
        }

        /**
         * Sets the weapon used for this damage
         * @param weapon the weapon used
         * @return this builder for chaining
         */
        public Builder weapon(SkyblockItem weapon) {
            this.weapon = weapon;
            return this;
        }

        /**
         * Sets the ability used for this damage
         * @param ability the ability used
         * @return this builder for chaining
         */
        public Builder ability(MagicAbility ability) {
            this.ability = ability;
            return this;
        }

        /**
         * Adds an attachment to this damage instance
         * @param <T> The type of the attachment
         * @param type The class of the attachment for retrieval
         * @param attachment The attachment object
         * @return this builder for chaining
         */
        public <T> Builder attachment(Class<T> type, T attachment) {
            this.attachments.put(type, attachment);
            return this;
        }

        /**
         * Sets all attachments from the provided map
         * @param attachments Map of attachments to include
         * @return this builder for chaining
         */
        public Builder withAttachments(Map<Class<?>, Object> attachments) {
            this.attachments.putAll(attachments);
            return this;
        }

        /**
         * Builds a new SkyblockDamage instance with the configured values
         * @return the new SkyblockDamage instance
         * @throws IllegalStateException if source or target are null
         */
        public SkyblockDamage build() {
            if (sourceEntity == null) {
                throw new IllegalStateException("Source entity must be specified");
            }
            if (targetEntity == null) {
                throw new IllegalStateException("Target entity must be specified");
            }
            return new SkyblockDamage(this);
        }
    }
}