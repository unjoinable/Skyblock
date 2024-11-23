package com.github.unjoinable.skyblock.user.sidebar;

import com.github.unjoinable.skyblock.Skyblock;
import com.github.unjoinable.skyblock.time.Season;
import com.github.unjoinable.skyblock.time.SkyblockStandardTime;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DefaultSidebar implements SkyblockSidebar {
    //statics
    private static final Component TITLE = Component.text("SKYBLOCK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD);
    private static final char DAY = '☀';
    private static final char NIGHT = '☽';
    private static final char LOCATION = '⏣';
    private static final Component LOC_COMPONENT = Component.text(LOCATION, NamedTextColor.GRAY);


    private final SkyblockStandardTime skyblockStandardTime;
    private final SkyblockPlayer player;

    public DefaultSidebar(@NotNull SkyblockPlayer player) {
        this.player = player;
        this.skyblockStandardTime = Skyblock.getSkyblockStandardTime();
    }


    @Override
    public @NotNull Component title() {
        return TITLE;
    }

    @Override
    public @NotNull List<Component> lines() {
        List<Component> lines = new ArrayList<>();
        lines.add(getDateLine());
        lines.add(Component.empty());
        lines.add(getSeasonDateLine());
        lines.add(getTimeLine());
        lines.add(getLocationLine());
        lines.add(Component.empty());
        lines.add(getPurseLine());
        lines.add(getBitsLine());
        lines.add(Component.empty());
        lines.add(getNetworkLine());
        return lines;
    }

    @Override
    public void init() {
        build().addViewer(player);
    }

    private Component getDateLine() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String formattedDate = localDate.format(formatter);

        return Component.text(formattedDate, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
    }

    private Component getSeasonDateLine() {
        Season season = skyblockStandardTime.getSeason();
        String skyblockDate = StringUtils.toDoubleDigit(skyblockStandardTime.getDay());
        return Component.text(" " + season.getDisplayName() + " " + skyblockDate, NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false);

    }

    private Component getTimeLine() {
        //added a space for the symbol
        Component skyblockTime = Component.text(" " + skyblockStandardTime.getTime() + " ", NamedTextColor.GRAY);
        Component symbol;
        if (skyblockStandardTime.isDayTime()) {
            symbol = Component.text(DAY, NamedTextColor.YELLOW);
        } else {
            symbol = Component.text(NIGHT, NamedTextColor.AQUA);
        }

        return skyblockTime.append(symbol);
    }

    private Component getLocationLine() {
        //not implemented
        return Component.text("").append(LOC_COMPONENT).append(Component.text(" Basement", NamedTextColor.AQUA));
    }

    private Component getPurseLine() {
        return Component.text("Purse: ", NamedTextColor.WHITE).append(Component.text(player.getCoins(), NamedTextColor.GOLD));
    }

    private Component getBitsLine() {
        return Component.text("Bits: ", NamedTextColor.WHITE).append(Component.text(player.getBits(), NamedTextColor.AQUA));
    }

    private Component getNetworkLine() {
        return Component.text("localhost", NamedTextColor.YELLOW);
    }
}
