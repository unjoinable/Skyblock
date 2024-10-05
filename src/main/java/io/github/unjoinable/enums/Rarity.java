package io.github.unjoinable.enums;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
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
    ;

    private final TextColor color;

    Rarity(TextColor color) {
        this.color = color;
    }

    /**
     * Finds the string in rarity enum.
     * @param string String which will be looked up in the enum.
     * @return Potentially a valid rarity.
     * @throws IllegalArgumentException If the provided string is not found in enum
     * @since 1.0.0
     */
    public static Rarity getRarity(@Nullable String string) {
        if (string == null) return Rarity.UNOBTAINABLE;;
        try {
            return Rarity.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) { //ignored
            return Rarity.UNOBTAINABLE;
        }
    }

    /**
     * @return The color of rarity
     * @since 1.0.0
     */
    public TextColor getColor() {
        return color;
    }

    /**
     * In case its already ADMIN it returns ADMIN
     * @return Upgraded rarity
     * @since 1.0.0
     */
    public Rarity upgrade() {
        if (this.ordinal() == Rarity.VERY_SPECIAL.ordinal()) return this;
        return values()[this.ordinal() - 1];
    }

    /**
     * In case its already COMMON it returns COMMON
     * @return Degraded rarity
     * @since 1.0.0
     */
    public Rarity degrade() {
        if (this.ordinal() == Rarity.COMMON.ordinal()) return this;
        return values()[this.ordinal() + 1];
    }
}