package net.unjoinable.player;

import net.kyori.adventure.text.format.TextDecoration;
import net.unjoinable.ui.sidebar.SkyblockSidebar;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class MainSidebar extends SkyblockSidebar {

    public MainSidebar() {
        super(text("SKYBLOCK", YELLOW).decorate(TextDecoration.BOLD));

        addStaticLine(text("24/06/25", GRAY).append(text(" mini696f", DARK_GRAY)));
        addEmptyLine();
        addStaticLine(text(" Early Autumn 3rd", WHITE));
        addStaticLine(text(" 1:20pm", GRAY));
        addStaticLine(text(" Your Island", GREEN));
        addEmptyLine();
        addDynamicLine(player -> text("Purse: ", WHITE).append(text(player.getEconomySystem().getCoins(), GOLD)));
        addDynamicLine(player -> text("Bits: ", WHITE).append(text(player.getEconomySystem().getBits(), AQUA)));
        addEmptyLine();
        addStaticLine(text("localhost:25565"));
    }
}
