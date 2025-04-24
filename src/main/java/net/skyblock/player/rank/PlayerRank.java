package net.skyblock.player.rank;

import net.skyblock.utils.MiniString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


public enum PlayerRank {
    OWNER("OWNER", NamedTextColor.RED, MiniString.component("<red>[OWNER]")),
    ADMIN("ADMIN", NamedTextColor.RED, MiniString.component("<red>[ADMIN]")),
    GAME_MASTER("GM", NamedTextColor.DARK_GREEN, MiniString.component("<dark_green>[GM]")),
    YOUTUBE("YOUTUBE", NamedTextColor.RED, MiniString.component("<red>[<white>YOUTUBE</white>]")),
    MVP_PLUS_PLUS("MVP++", NamedTextColor.GOLD, MiniString.component("<gold>[MVP<black>++</black>]")),
    MVP_PLUS("MVP+", NamedTextColor.AQUA, MiniString.component("<aqua>[MVP<black>+</black>]")),
    MVP("MVP", NamedTextColor.AQUA, MiniString.component("<aqua>[MVP]")),
    VIP_PLUS("VIP+", NamedTextColor.GREEN, MiniString.component("<green>[VIP<gold>+</gold>]")),
    VIP("VIP", NamedTextColor.GREEN, MiniString.component("<green>[VIP]")),
    DEFAULT("", NamedTextColor.GRAY, MiniString.component("")),
    ;

    private final String prefix;
    private final NamedTextColor color;
    private final Component componentPrefix;

    PlayerRank (String prefix, NamedTextColor color, Component componentPrefix) {
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
