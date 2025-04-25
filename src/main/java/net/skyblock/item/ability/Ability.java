package net.skyblock.item.ability;

/**
 * Represents a single ability on an item
 */
public class Ability implements DamageableAbility {
    private final String id;
    private final String name;
    private final String description;
    private final AbilityType type;
    private final int manaCost;
    private final int cooldownTicks;
    private final double baseAbilityDamage;
    private final double abilityScaling;

    public Ability(String id, String name, String description, AbilityType type,
                   int manaCost, int cooldownTicks, double baseAbilityDamage,
                   double abilityScaling) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
        this.baseAbilityDamage = baseAbilityDamage;
        this.abilityScaling = abilityScaling;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public AbilityType getType() {
        return type;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    @Override
    public double getBaseAbilityDamage() {
        return baseAbilityDamage;
    }

    @Override
    public double getAbilityScaling() {
        return abilityScaling;
    }

    /**
     * Builder for creating Ability instances
     */
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private AbilityType type = AbilityType.RIGHT_CLICK;
        private int manaCost = 0;
        private int cooldownTicks = 0;
        private double baseAbilityDamage = 0;
        private double abilityScaling = 1.0;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(AbilityType type) {
            this.type = type;
            return this;
        }

        public Builder manaCost(int manaCost) {
            this.manaCost = manaCost;
            return this;
        }

        public Builder cooldownTicks(int cooldownTicks) {
            this.cooldownTicks = cooldownTicks;
            return this;
        }

        public Builder baseAbilityDamage(double baseAbilityDamage) {
            this.baseAbilityDamage = baseAbilityDamage;
            return this;
        }

        public Builder abilityScaling(double abilityScaling) {
            this.abilityScaling = abilityScaling;
            return this;
        }

        public Ability build() {
            if (id == null || id.isEmpty()) {
                throw new IllegalStateException("Ability ID must be specified");
            }
            if (name == null || name.isEmpty()) {
                throw new IllegalStateException("Ability name must be specified");
            }

            return new Ability(id, name, description, type, manaCost, cooldownTicks,
                    baseAbilityDamage, abilityScaling);
        }
    }

    /**
     * Type of ability activation
     */
    public enum AbilityType {
        LEFT_CLICK,
        RIGHT_CLICK,
        SNEAK,
        FULL_SET_BONUS,
        PASSIVE
    }
}