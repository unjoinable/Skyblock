package net.skyblock.stats.calculator;

import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.ability.MagicAbility;
import net.skyblock.stats.definition.SkyblockDamage;
import net.skyblock.stats.definition.Statistic;
import net.skyblock.stats.holder.CombatEntity;
import net.skyblock.stats.definition.DamageType;

import java.util.Random;

/**
 * A modular damage calculator that follows Hypixel's damage formulas.
 * Enhanced with proper damage type handling and modular components.
 */
public class DamageCalculator {
    private static final Random RANDOM = new Random();

    // Base values for damage calculations
    private static final double BASE_PHYSICAL_VALUE = 5.0;
    private static final double CRIT_CHANCE_CAP = 100.0;
    private static final double FEROCITY_HIT_CHANCE = 100.0;

    private DamageCalculator() {}

    /**
     * Calculates damage based on Hypixel's damage formula.
     *
     * @param source     The entity dealing damage
     * @param target     The entity receiving damage
     * @param weapon     Optional weapon used (can be null)
     * @param damageType The type of damage being dealt
     * @return A built SkyblockDamage object with calculated values
     */
    public static SkyblockDamage calculateDamage(CombatEntity source, CombatEntity target,
                                                 SkyblockItem weapon, DamageType damageType) {
        StatProfile sourceStats = source.getStatProfile();

        // Get base stats
        double baseDamage = sourceStats.get(Statistic.DAMAGE);
        double strength = sourceStats.get(Statistic.STRENGTH);
        double critChance = Math.min(sourceStats.get(Statistic.CRIT_CHANCE), CRIT_CHANCE_CAP);
        double critDamage = sourceStats.get(Statistic.CRIT_DAMAGE);
        double ferocity = sourceStats.get(Statistic.FEROCITY);

        // Check for critical hit if this damage type can crit
        boolean isCritical = damageType.canCrit() && RANDOM.nextDouble() * 100 <= critChance;

        // Calculate base damage value
        double calculatedDamage = (BASE_PHYSICAL_VALUE + baseDamage) * (1 + strength / 100);

        double additiveMultiplier = 1d;
        double multiplicativeMultiplier = 1d;
        double bonusModifiers = 1d;

        calculatedDamage =  calculatedDamage * additiveMultiplier * multiplicativeMultiplier + bonusModifiers;

        if (isCritical) {
            calculatedDamage *= (1 + critDamage / 100);
        }

        // Full damage calculation with additional multipliers
        double rawDamage = calculatedDamage;


        // Calculate ferocity hits (only for physical damage types)
        int ferocityHits = 0;
        if (damageType.isPhysical() && ferocity > 0) {
            ferocityHits = calculateFerocityHits(ferocity);
        }

        //TODO add ferocity handling

        return SkyblockDamage.builder()
                .rawDamage(rawDamage)
                .criticalHit(isCritical)
                .damageType(damageType)
                .sourceEntity(source)
                .targetEntity(target)
                .ferocityHits(ferocityHits)
                .weapon(weapon)
                .build();
    }

    /**
     * Calculates ability damage based on Hypixel's ability damage formula.
     *
     * @param source  The entity using the ability
     * @param target  The entity receiving damage
     * @param ability The ability being used
     * @return A built SkyblockDamage object with calculated values
     */
    public static SkyblockDamage calculateAbilityDamage(CombatEntity source, CombatEntity target, MagicAbility ability) {
        StatProfile sourceStats = source.getStatProfile();

        // Get needed stats
        double intelligence = sourceStats.get(Statistic.INTELLIGENCE);
        double abilityDamageBonus = sourceStats.get(Statistic.ABILITY_DAMAGE);

        // Get ability parameters
        double baseAbilityDamage = ability.baseAbilityDamage();
        double abilityScaling = ability.abilityScaling();
        DamageType damageType = ability.damageType();

        // Base ability damage calculation
        double calculatedDamage = baseAbilityDamage * (1 + (intelligence / 100) * abilityScaling);

        // Apply ability damage bonus (from Ability Damage stat)
        calculatedDamage *= (1 + abilityDamageBonus / 100);

        // Apply additiveMultiplier for abilities
        double additiveMultiplier = 1;

        // Apply multiplicativeMultiplier for abilities
        double multiplicativeMultiplier = 1;

        // Apply bonusModifiers for abilities
        double bonusModifiers = 1;

        // Full damage calculation
        double rawDamage = (calculatedDamage * additiveMultiplier * multiplicativeMultiplier + bonusModifiers);

        // Build the damage object
        return SkyblockDamage.builder()
                .rawDamage(rawDamage)
                .criticalHit(false) // Abilities don't crit
                .damageType(damageType)
                .sourceEntity(source)
                .targetEntity(target)
                .ability(ability)
                .build();
    }

    /**
     * Calculates the number of ferocity hits based on ferocity stat
     *
     * @param ferocity The ferocity stat value
     * @return The number of ferocity hits to perform
     */
    private static int calculateFerocityHits(double ferocity) {
        int guaranteedHits = (int) Math.floor(ferocity);
        double chanceForExtraHit = (ferocity - guaranteedHits) * FEROCITY_HIT_CHANCE;

        if (RANDOM.nextDouble() * 100 <= chanceForExtraHit) {
            guaranteedHits++;
        }
        return guaranteedHits;
    }
}