package io.github.unjoinable.skyblock.user.sidebar;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

public class DefaultScoreboard extends Sidebar {
    private static final Component TITLE = Component.text("SKYBLOCK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD);
    private final SkyblockPlayer player;

    public DefaultScoreboard(@NotNull SkyblockPlayer player) {
        super(TITLE);
        this.player = player;
    }
}
