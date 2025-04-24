package net.skyblock.item.enums;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the rarity tier of an item.
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

    /**
     * Constructor for the enum constants to assign a color to each rarity tier.
     * <p>
     * This constructor associates a specific {@link TextColor} with each rarity tier, which determines the color
     * that will be used for the item when displayed in-game.
     * </p>
     *
     * @param color The {@link TextColor} associated with the rarity tier.
     */
    Rarity(TextColor color) {
        this.color = color;
    }

    /**
     * Returns the Adventure {@link TextColor} associated with this rarity.
     *
     * @return the rarity's color
     */
    public TextColor getColor() {
        return color;
    }

    /**
     * Attempts to resolve a Rarity enum from a string name.
     *
     * @param string the name of the rarity (case-insensitive)
     * @return a valid Rarity, or {@link #UNOBTAINABLE} if not found or null
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
     */
    public Rarity upgrade() {
        return this.ordinal() > 0
                ? values()[this.ordinal() - 1]
                : this;
    }

    /**
     * @return the next lower rarity tier, or this if it's already the lowest
     */
    public Rarity degrade() {
        int next = this.ordinal() + 1;
        return next < values().length ? values()[next] : this;
    }
}
