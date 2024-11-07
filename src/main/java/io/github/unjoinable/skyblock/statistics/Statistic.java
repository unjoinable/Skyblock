package io.github.unjoinable.skyblock.statistics;


import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public enum Statistic {
    HEALTH("Health", GREEN, RED, false, "❤", 100),
    DEFENSE("Defense", GREEN, GREEN, false, "❈" , 0),
    STRENGTH("Strength", RED, RED, false, "❁", 0),
    INTELLIGENCE("Intelligence", GREEN, AQUA, false, "✎", 100),
    CRIT_CHANCE("Crit Chance", RED, BLUE, true, "☣", 30),
    CRIT_DAMAGE("Crit Damage", RED, BLUE, true, "☠", 50),
    BONUS_ATTACK_SPEED("Bonus Attack Speed", RED, YELLOW, true, "⚔", 0),
    ABILITY_DAMAGE("ItemAbility Damage", RED, RED, true, "๑", 0),
    TRUE_DEFENSE("True Defense", GREEN, WHITE, false, "❂", 0),
    FEROCITY("Ferocity", GREEN, RED, false, "⫽", 0),
    HEALTH_REGEN("Health Regen", GREEN, RED, false, "❣", 100),
    VITALITY("Vitality", GREEN, DARK_RED,  false, "♨", 100),
    MENDING("Mending", GREEN, GREEN, false, "☄", 100),
    SWING_RANGE("Swing Range", YELLOW, YELLOW, false, "Ⓢ", 3),
    MINING_SPEED("Mining Speed", GREEN, GOLD, false, "⸕", 0),
    MINING_FORTUNE("Mining Fortune", GREEN, GOLD, false, "☘", 0),
    FARMING_FORTUNE("Farming Fortune", GREEN, GOLD, false, "☘", 0),
    FORAGING_FORTUNE("Foraging Fortune", GREEN, GOLD, false, "☘", 0),
    BREAKING_POWER("Breaking Power", GREEN, DARK_GREEN, false, "Ⓟ", 0),
    PRISTINE("Pristine", GREEN, DARK_PURPLE, false, "✧", 0),
    SPEED("Speed", GREEN, WHITE, false, "✦", 100),
    MAGIC_FIND("Magic Find", GREEN, AQUA, false, "✯", 0),
    PET_LUCK("Pet Luck", GREEN, LIGHT_PURPLE, false, "♣", 0),
    SEA_CREATURE_CHANCE("Sea Creature Chance", RED, DARK_PURPLE, true, "α", 2),
    FISHING_SPEED("Fishing Speed", GREEN, AQUA, false, "☂", 0),
    COLD_RESISTANCE("Cold Resistance", GREEN, AQUA, false, "❄", 0),
    BONUS_PEST_CHANCE("Bonus Pest Chance", GREEN, DARK_GREEN, true, "ൠ", 0),

    // Other Stats
    DAMAGE("Damage", RED, RED, false, "❁", 5),
    ;


    private final String displayName;
    private final TextColor loreColor;
    private final TextColor displayColor;
    private final Boolean isPercentage;
    private final String symbol;

    private final int baseValue;

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
}