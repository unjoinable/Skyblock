package net.unjoinable.skyblock.item.enums;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

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
    Rarity(@NotNull TextColor color) {
        this.color = color;
    }

    /**
     * Returns the Adventure {@link TextColor} associated with this rarity.
     *
     * @return the rarity's color
     */
    public @NotNull TextColor color() {
        return color;
    }

    /**
     * @return the next higher rarity tier, or this if it's already the highest
     */
    public @NotNull Rarity upgrade() {
        return this.ordinal() > 0
                ? values()[this.ordinal() - 1]
                : this;
    }

    /**
     * @return the next lower rarity tier, or this if it's already the lowest
     */
    public @NotNull Rarity degrade() {
        int next = this.ordinal() + 1;
        return next < values().length ? values()[next] : this;
    }
}
