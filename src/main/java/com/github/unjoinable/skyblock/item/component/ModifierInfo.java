package com.github.unjoinable.skyblock.item.component;

import com.github.unjoinable.skyblock.item.enums.ModifierType;
import net.kyori.adventure.text.format.TextColor;

/**
 * Holds information about a specific item modifier, including its type, value,
 * the characters used for its display brackets, and its text color.
 */
public class ModifierInfo {
    private final ModifierType modifierType;
    private final float value;
    private final char openBracket;
    private final char closeBracket;
    private final TextColor color;

    /**
     * Constructs a new ModifierInfo.
     *
     * @param type The type of modifier.
     * @param value The numerical value associated with the modifier.
     * @param openBracket The character used for the opening bracket in its display.
     * @param closeBracket The character used for the closing bracket in its display.
     * @param color The text color for displaying the modifier.
     */
    public ModifierInfo(ModifierType type, float value, char openBracket, char closeBracket, TextColor color) {
        this.modifierType = type;
        this.value = value;
        this.openBracket = openBracket;
        this.closeBracket = closeBracket;
        this.color = color;
    }

    /**
     * Gets the type of the modifier.
     * @return The modifier type.
     */
    public ModifierType getModifierType() {
        return modifierType;
    }

    /**
     * Gets the numerical value of the modifier.
     * @return The modifier value.
     */
    public float getValue() {
        return value;
    }

    /**
     * Gets the character used for the opening bracket.
     * @return The opening bracket character.
     */
    public char getOpenBracket() {
        return openBracket;
    }

    /**
     * Gets the character used for the closing bracket.
     * @return The closing bracket character.
     */
    public char getCloseBracket() {
        return closeBracket;
    }

    /**
     * Gets the text color of the modifier.
     * @return The modifier color.
     */
    public TextColor getColor() {
        return color;
    }
}