package com.github.unjoinable.skyblock.user.permissions;

import com.github.unjoinable.skyblock.util.MiniString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("unused")
/*
Note: I am putting prefixes in enum itself since its constant and doesn't change so why do we make it again???
 */
public enum PlayerRank {
    OWNER("OWNER", NamedTextColor.RED, MiniString.toComponent("<red>[OWNER]")),
    ADMIN("ADMIN", NamedTextColor.RED, MiniString.toComponent("<red>[ADMIN]")),
    GAME_MASTER("GM", NamedTextColor.DARK_GREEN, MiniString.toComponent("<dark_green>[GM]")),
    YOUTUBE("YOUTUBE", NamedTextColor.RED, MiniString.toComponent("<red>[<white>YOUTUBE</white>]")),
    MVP_PLUS_PLUS("MVP++", NamedTextColor.GOLD, MiniString.toComponent("<gold>[MVP<black>++</black>]")),
    MVP_PLUS("MVP+", NamedTextColor.AQUA, MiniString.toComponent("<aqua>[MVP<black>+</black>]")),
    MVP("MVP", NamedTextColor.AQUA, MiniString.toComponent("<aqua>[MVP]")),
    VIP_PLUS("VIP+", NamedTextColor.GREEN, MiniString.toComponent("<green>[VIP<gold>+</gold>]")),
    VIP("VIP", NamedTextColor.GREEN, MiniString.toComponent("<green>[VIP]")),
    DEFAULT("", NamedTextColor.GRAY, MiniString.toComponent("")),
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
}
