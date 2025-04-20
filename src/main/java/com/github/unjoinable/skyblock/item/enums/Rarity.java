package com.github.unjoinable.skyblock.item.enums;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the rarity tier of an item.
 *
 * @since 1.0.0
 */
public enum Rarity {
    ADMIN(NamedTextColor.DARK_RED),
    VERY_SPECIAL(NamedTextColor.RED),
    SPECIAL(NamedTextColor.RED),
    DIVINE(NamedTextColor.AQUA),
    MYTHIC(NamedTextColor.LIGHT_PURPLE),
    LEGENDARY(NamedTextColor.GOLD),
    EPIC(NamedTextColor.DARK_PURPLE),
    RARE(NamedTextColor.BLUE),
    UNCOMMON(NamedTextColor.GREEN),
    COMMON(NamedTextColor.WHITE),
    UNOBTAINABLE(NamedTextColor.GRAY);

    private final TextColor color;

    Rarity(TextColor color) {
        this.color = color;
    }

    /**
     * Returns the Adventure {@link TextColor} associated with this rarity.
     *
     * @return the rarity's color
     * @since 1.0.0
     */
    public TextColor getColor() {
        return color;
    }

    /**
     * Attempts to resolve a Rarity enum from a string name.
     *
     * @param string the name of the rarity (case-insensitive)
     * @return a valid Rarity, or {@link #UNOBTAINABLE} if not found or null
     * @since 1.0.0
     */
    public static Rarity getRarity(@Nullable String string) {
        if (string == null) return UNOBTAINABLE;
        try {
            return Rarity.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNOBTAINABLE;
        }
    }

    /**
     * @return the next higher rarity tier, or this if it's already the highest
     * @since 1.0.0
     */
    public Rarity upgrade() {
        return this.ordinal() > 0
                ? values()[this.ordinal() - 1]
                : this;
    }

    /**
     * @return the next lower rarity tier, or this if it's already the lowest
     * @since 1.0.0
     */
    public Rarity degrade() {
        int next = this.ordinal() + 1;
        return next < values().length ? values()[next] : this;
    }
}
