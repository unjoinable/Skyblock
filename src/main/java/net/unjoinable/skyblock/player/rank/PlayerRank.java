package net.unjoinable.skyblock.player.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

/**
 * Enum representing different player ranks in the Skyblock server.
 *
 * <p>Each rank has an associated prefix, color, and component representation for display purposes.
 * Ranks are ordered from highest to lowest priority, with OWNER being the highest rank and DEFAULT
 * being the lowest.</p>
 *
 * <p>The enum provides methods to retrieve rank properties and compare rank hierarchies using the
 * {@link #isAtLeast(PlayerRank)} method, which uses the enum's ordinal position for comparison.</p>
 */
public enum PlayerRank {
    HYPIXEL_STAFF("HYPIXEL STAFF", RED, textOfChildren(text("[", RED), text("ዞ", GOLD, BOLD), text("]", RED))),
    YOUTUBE("YOUTUBE", RED, textOfChildren(text("[", RED), text("YOUTUBE", WHITE), text("]", RED))),
    MVP_PLUS_PLUS("MVP++", GOLD, textOfChildren(text("[MVP", GOLD), text("++", BLACK), text("]", GOLD))),
    MVP_PLUS("MVP+", AQUA, textOfChildren(text("[MVP", AQUA), text("+", BLACK), text("]", AQUA))),
    MVP("MVP", AQUA, text("[MVP]", AQUA)),
    VIP_PLUS("VIP+", GREEN, textOfChildren(text("[VIP", GREEN), text("+", GOLD), text("]", GREEN))),
    VIP("VIP", GREEN, text("[VIP]", GREEN)),
    DEFAULT("", GRAY, empty()),
    ;

    private final String prefix;
    private final NamedTextColor color;
    private final Component componentPrefix;

    /**
     * Constructor for the enum constants to assign a prefix, color, and component prefix for each rank.
     * <p>
     * This constructor is used to initialize each player's rank with the following properties:
     * - A string prefix that represents the rank name.
     * - A color associated with the rank for display purposes.
     * - A component-based prefix that can be used for in-game chat or other displays.
     * </p>
     *
     * @param prefix The string prefix associated with the rank (e.g., "OWNER", "ADMIN").
     * @param color The {@link NamedTextColor} associated with this rank for display purposes.
     * @param componentPrefix The {@link Component} representation of the prefix, used for formatted display in the game.
     */
    PlayerRank(String prefix, NamedTextColor color, Component componentPrefix) {
        this.prefix = prefix;
        this.color = color;
        this.componentPrefix = componentPrefix;
    }

    /**
     * @return Get the color of rank
     */
    public NamedTextColor getColor() {
        return color;
    }

    /**
     * @return Get the prefix of rank
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @return Get component form of prefix!
     */
    public Component getComponentPrefix() {
        return componentPrefix;
    }

    /**
     * Compares this rank's position in the enum to another.
     *
     * @param other The other rank to compare to
     * @return true if this rank is higher than or equal to the other rank
     */
    public boolean isAtLeast(PlayerRank other) {
        return this.ordinal() <= other.ordinal();
    }
}

