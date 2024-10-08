package io.github.unjoinable.skyblock.statistics;


import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @since 1.0.0
 */
public enum Statistic {
    HEALTH("Health", "green", "red", false, "❤", 100),
    DEFENSE("Defense", "green", "green", false, "❈" , 0),
    STRENGTH("Strength", "red", "red", false, "❁", 0),
    INTELLIGENCE("Intelligence", "green", "aqua", false, "✎", 100),
    CRIT_CHANCE("Crit Chance", "red", "blue", true, "☣", 30),
    CRIT_DAMAGE("Crit Damage", "red", "blue", true, "☠", 50),
    BONUS_ATTACK_SPEED("Bonus Attack Speed", "red", "yellow", true, "⚔", 0),
    ABILITY_DAMAGE("ItemAbility Damage", "red", "red", true, "๑", 0),
    TRUE_DEFENSE("True Defense", "green", "white", false, "❂", 0),
    FEROCITY("Ferocity", "green", "red", false, "⫽", 0),
    HEALTH_REGEN("Health Regen", "green", "red", false, "❣", 100),
    VITALITY("Vitality", "green", "dark_red",  false, "♨", 100),
    MENDING("Mending", "green", "green", false, "☄", 100),
    SWING_RANGE("Swing Range", "yellow", "yellow", false, "Ⓢ", 3),
    MINING_SPEED("Mining Speed", "green", "gold", false, "⸕", 0),
    MINING_FORTUNE("Mining Fortune", "green", "gold", false, "☘", 0),
    FARMING_FORTUNE("Farming Fortune", "green", "gold", false, "☘", 0),
    FORAGING_FORTUNE("Foraging Fortune", "green", "gold", false, "☘", 0),
    BREAKING_POWER("Breaking Power", "green", "dark_green", false, "Ⓟ", 0),
    PRISTINE("Pristine", "green", "dark_purple", false, "✧", 0),
    SPEED("Speed", "green", "white", false, "✦", 100),
    MAGIC_FIND("Magic Find", "green", "aqua", false, "✯", 0),
    PET_LUCK("Pet Luck", "green", "light_purple", false, "♣", 0),
    SEA_CREATURE_CHANCE("Sea Creature Chance", "red", "dark_blue", true, "α", 2),
    FISHING_SPEED("Fishing Speed", "green", "aqua", false, "☂", 0),
    COLD_RESISTANCE("Cold Resistance", "green", "aqua", false, "❄", 0),
    BONUS_PEST_CHANCE("Bonus Pest Chance", "green", "dark_green", true, "ൠ", 0),

    // Other Stats
    DAMAGE("Damage", "red", "red", false, "❁", 5),
    ;


    private final String displayName;
    private final String loreColor;
    private final String displayColor;
    private final Boolean isPercentage;
    private final String symbol;

    private final int baseValue;

    private static final Collection<Statistic> VALUES = Arrays.asList(values());

    Statistic(@NotNull String displayName,
              @NotNull String loreColor,
              @NotNull String displayColor,
              boolean isPercentage,
              @NotNull String symbol,
              int baseValue) {
        this.displayName = displayName;
        this.loreColor = loreColor;
        this.displayColor = displayColor;
        this.isPercentage = isPercentage;
        this.symbol = symbol;
        this.baseValue = baseValue;
    }

    /**
     * @return All Statistics as a collection.
     * @since 1.0.0
     */
    public static Collection<Statistic> getValues() {
        //for cache-ing them
        return VALUES;
    }

    /**
     * @return The symbol of the statistic.
     * @since 1.0.0
     */
    public @NotNull String getSymbol() {
        return symbol;
    }

    /**
     * @return If it uses a % symbol in the lore.
     * @since 1.0.0
     */
    public Boolean getPercentage() {
        return isPercentage;
    }

    /**
     * @return Lore-able display name of statistic.
     * @since 1.0.0
     */
    public @NotNull String getDisplayName() {
        return displayName;
    }

    /**
     * @return Display color of that statistic,  <color> return is color
     * @since 1.0.0
     */
    public @NotNull String getDisplayColor() {
        return displayColor;
    }

    /**
     * @return Returns color based on if its passive or aggressive.
     *         RED/GREEN (Yellow for Swing Range)
     *         <color> return is color.
     * @since 1.0.0
     */
    public @NotNull String getLoreColor() {
        return loreColor;
    }

    /**
     * @return The base value of statistic.
     * @since 1.0.0
     */
    public int getBaseValue() {
        return baseValue;
    }
}