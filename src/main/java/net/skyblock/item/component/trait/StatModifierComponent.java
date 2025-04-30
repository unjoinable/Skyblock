package net.skyblock.item.component.trait;

import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.enums.ModifierType;
import net.skyblock.stats.StatProfile;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for components that modify item stats.
 */
public interface StatModifierComponent extends ItemComponent {
    /**
     * Gets the type of this stat modifier.
     * @return The modifier type
     */
    @NotNull ModifierType getModifierType();

    /**
     * Gets the stat profile containing the modifications.
     * @return The stat profile with modifications
     */
    @NotNull StatProfile getStatProfile(ComponentContainer container);

    /**
     * Gets the bracket type for this modifier.
     * Default is square brackets.
     * @return The bracket type character (e.g., '[', '(', etc.)
     */
    default char getOpenBracket() {
        return '[';
    }

    /**
     * Gets the closing bracket that matches the opening one.
     * @return The closing bracket character
     */
    default char getCloseBracket() {
        char open = getOpenBracket();
        return switch (open) {
            case '[' -> ']';
            case '(' -> ')';
            case '{' -> '}';
            case '<' -> '>';
            default -> ']'; // Default to square bracket if unknown
        };
    }

    /**
     * Gets the color for this modifier in lore.
     * Default is white.
     * @return The TextColor for this modifier
     */
    default TextColor getModifierColor() {
        return NamedTextColor.WHITE;
    }
}