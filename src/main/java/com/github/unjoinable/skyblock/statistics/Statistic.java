package com.github.unjoinable.skyblock.statistics;


import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public enum Statistic {
    DAMAGE("Damage", RED, RED, false, "❁", 0),
    STRENGTH("Strength", RED, RED, false, "❁", 0),
    CRIT_CHANCE("Crit Chance", RED, BLUE, true, "☣", 30, 100),
    CRIT_DAMAGE("Crit Damage", RED, BLUE, true, "☠", 50),
    BONUS_ATTACK_SPEED("Bonus Attack Speed", RED, YELLOW, true, "⚔", 0),
    ABILITY_DAMAGE("Ability Damage", RED, RED, true, "๑", 0),
    SWING_RANGE("Swing Range", YELLOW, YELLOW, false, "Ⓢ", 3),
    HEALTH("Health", GREEN, RED, false, "❤", 100),
    DEFENSE("Defense", GREEN, GREEN, false, "❈" , 0),
    SPEED("Speed", GREEN, WHITE, false, "✦", 100, 600),
    INTELLIGENCE("Intelligence", GREEN, AQUA, false, "✎", 100),
    MAGIC_FIND("Magic Find", GREEN, AQUA, false, "✯", 0),
    PET_LUCK("Pet Luck", GREEN, LIGHT_PURPLE, false, "♣", 0),
    TRUE_DEFENSE("True Defense", GREEN, WHITE, false, "❂", 0),
    FEROCITY("Ferocity", GREEN, RED, false, "⫽", 0),
    MINING_SPEED("Mining Speed", GREEN, GOLD, false, "⸕", 0),
    BREAKING_POWER("Breaking Power", GREEN, DARK_GREEN, false, "Ⓟ", 0),
    PRISTINE("Pristine", GREEN, DARK_PURPLE, false, "✧", 0),
    MINING_FORTUNE("Mining Fortune", GREEN, GOLD, false, "☘", 0),
    FORAGING_FORTUNE("Foraging Fortune", GREEN, GOLD, false, "☘", 0),
    FARMING_FORTUNE("Farming Fortune", GREEN, GOLD, false, "☘", 0),
    SEA_CREATURE_CHANCE("Sea Creature Chance", RED, DARK_AQUA, true, "α", 20),
    FISHING_SPEED("Fishing Speed", GREEN, AQUA, false, "☂", 0),

    //Wisdom Stats
    ALCHEMY_WISDOM("Alchemy Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    CARPENTRY_WISDOM("Carpentry Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    COMBAT_WISDOM("Combat Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    ENCHANTING_WISDOM("Enchanting Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    FARMING_WISDOM("Farming Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    FISHING_WISDOM("Fishing Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    FORAGING_WISDOM("Foraging Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    MINING_WISDOM("Mining Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    RUNECRAFTING_WISDOM("Runecrafting Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    SOCIAL_WISDOM("Social Wisdom", GREEN, DARK_AQUA, false, "☯", 0),
    TAMING_WISDOM("Taming Wisdom", GREEN, DARK_AQUA, false, "☯", 0),

    HEALTH_REGEN("Health Regen", GREEN, RED, false, "❣", 100),
    COLD_RESISTANCE("Cold Resistance", GREEN, AQUA, false, "❄", 0),
    VITALITY("Vitality", GREEN, DARK_RED,  false, "♨", 100),
    MENDING("Mending", GREEN, GREEN, false, "☄", 100),

    //Stats I don't know much about.
    MINING_SPREAD("Mining Spread", GREEN, YELLOW, false, "▚", 0),
    GEMSTONE_SPREAD("Gemstone Spread", GREEN, YELLOW, false, "▚", 0),
    DOUBLE_HOOK_CHANCE("Double Hook Chance", GREEN, DARK_BLUE, true, "⚓", 0),
    BONUS_PEST_CHANCE("Bonus Pest Chance", GREEN, DARK_GREEN, true, "ൠ", 0),
    ;


    private final String displayName;
    private final TextColor loreColor;
    private final TextColor displayColor;
    private final Boolean isPercentage;
    private final String symbol;

    private final int baseValue;
    private final boolean isCapped;
    private final int capValue;

    private static final Collection<Statistic> VALUES = Arrays.asList(values());

    Statistic(@NotNull String displayName,
              @NotNull NamedTextColor loreColor,
              @NotNull NamedTextColor displayColor,
              boolean isPercentage,
              @NotNull String symbol,
              int baseValue) {
        this.displayName = displayName;
        this.loreColor = loreColor;
        this.displayColor = displayColor;
        this.isPercentage = isPercentage;
        this.symbol = symbol;
        this.baseValue = baseValue;
        this.isCapped = false;
        this.capValue = 0;
    }

    Statistic(@NotNull String displayName,
              @NotNull NamedTextColor loreColor,
              @NotNull NamedTextColor displayColor,
              boolean isPercentage,
              @NotNull String symbol,
              int baseValue,
              int capValue) {
        this.displayName = displayName;
        this.loreColor = loreColor;
        this.displayColor = displayColor;
        this.isPercentage = isPercentage;
        this.symbol = symbol;
        this.baseValue = baseValue;
        this.isCapped = true;
        this.capValue = capValue;
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
     * @return Display color of that statistic
     * @since 1.0.0
     */
    public @NotNull TextColor getDisplayColor() {
        return displayColor;
    }

    /**
     * @return Returns color based on if its passive or aggressive.
     *         RED/GREEN (Yellow for Swing Range)
     * @since 1.0.0
     */
    public @NotNull TextColor getLoreColor() {
        return loreColor;
    }

    /**
     * @return The base value of statistic.
     * @since 1.0.0
     */
    public int getBaseValue() {
        return baseValue;
    }

    public boolean isCapped() {
        return isCapped;
    }

    public int getCapValue() {
        return capValue;
    }

    public static Statistic byOrdinal(int ordinal) {
        return values()[ordinal];
    }
}