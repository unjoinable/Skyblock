package io.github.unjoinable.skyblock.user.sidebar;

import io.github.unjoinable.skyblock.Skyblock;
import io.github.unjoinable.skyblock.time.Season;
import io.github.unjoinable.skyblock.time.SkyblockStandardTime;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DefaultScoreboard extends Sidebar {
    private static final Component TITLE = Component.text("SKYBLOCK", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD);
    private final SkyblockStandardTime skyblockStandardTime;
    private final SkyblockPlayer player;

    //statics
    private static final char DAY = '☀';
    private static final char NIGHT = '☽';
    private static final char LOCATION = '⏣';

    private static final Component LOC_COMPONENT = Component.text(LOCATION, NamedTextColor.GRAY);

    public DefaultScoreboard(@NotNull SkyblockPlayer player) {
        super(TITLE);
        this.player = player;
        this.skyblockStandardTime = Skyblock.getSkyblockStandardTime();

        createLine(getDateLine());
        createLine(getSeasonDateLine());
        createLine(getTimeLine());
        createLine(getLocationLine());
        createLine(getPurseLine());
        createLine(getBitsLine());
        createLine(getNetworkLine());

    }

    public void init() {
        this.addViewer(player);
    }

    private ScoreboardLine getDateLine() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String formattedDate = localDate.format(formatter);

        return new ScoreboardLine("date",
                Component.text(formattedDate, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                1);
    }

    private ScoreboardLine getSeasonDateLine() {
        Season season = skyblockStandardTime.getSeason();
        String skyblockDate = StringUtils.toDoubleDigit(skyblockStandardTime.getDay());
        return new ScoreboardLine("season_date",
                Component.text(" " + season.getDisplayName() + " " + skyblockDate, NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false),
                3);
    }

    private ScoreboardLine getTimeLine() {
        //added a space for the symbol
        Component skyblockTime = Component.text(" " + skyblockStandardTime.getTime() + " ", NamedTextColor.GRAY);
        Component symbol;
        if (skyblockStandardTime.isDayTime()) {
            symbol = Component.text(DAY, NamedTextColor.YELLOW);
        } else {
            symbol = Component.text(NIGHT, NamedTextColor.AQUA);
        }

        return new ScoreboardLine("time",
                skyblockTime.append(symbol),
                4);
    }

    private ScoreboardLine getLocationLine() {
        //not implemented
        return new ScoreboardLine("location",
                Component.text("").append(LOC_COMPONENT).append(Component.text(" Basement", NamedTextColor.AQUA)),
                5
        );
    }

    private ScoreboardLine getPurseLine() {
        return new ScoreboardLine("purse",
                Component.text("Purse: ", NamedTextColor.WHITE).append(Component.text(player.getCoins(), NamedTextColor.GOLD)),
                7);
    }

    private ScoreboardLine getBitsLine() {
        return new ScoreboardLine("bits",
                Component.text("Bits: ", NamedTextColor.WHITE).append(Component.text(player.getBits(), NamedTextColor.AQUA)),
                8);
    }

    private ScoreboardLine getNetworkLine() {
        return new ScoreboardLine("network",
                Component.text("localhost", NamedTextColor.YELLOW),
                10);
    }

}
