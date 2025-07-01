package net.unjoinable.skyblock.player.ui.sidebar;

import net.kyori.adventure.text.format.TextDecoration;
import net.unjoinable.skyblock.level.Region;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.time.SkyblockStandardTime;
import net.unjoinable.skyblock.ui.sidebar.Sidebar;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.unjoinable.skyblock.utils.NumberUtils.formatDate;

public class PlayerSidebar extends Sidebar {

    public PlayerSidebar(SkyblockPlayer sPlayer, SkyblockStandardTime skyblockTime) {
        super(sPlayer, text("SKYBLOCK", YELLOW).decorate(TextDecoration.BOLD));

        addStaticLine(text("24/06/25", GRAY).append(text(" mini696f", DARK_GRAY)));
        addEmptyLine();
        addDynamicLine(_ -> text(" " + skyblockTime.getSeason() + " " + formatDate(skyblockTime.getDay()), WHITE));
        addDynamicLine(_ -> skyblockTime.getTimeComponent());
        addDynamicLine((player -> {
            Region region = player.getIsland().getRegion(player.getPosition());
            return textOfChildren(text(" ⏣ ", GRAY), region.displayName());
        }));
        addEmptyLine();
        addDynamicLine(player -> text("Purse: ", WHITE).append(text(player.getEconomySystem().getCoins(), GOLD)));
        addDynamicLine(player -> text("Bits: ", WHITE).append(text(player.getEconomySystem().getBits(), AQUA)));
        addEmptyLine();
        addStaticLine(text("mc.pickle.net", YELLOW));
    }
}
