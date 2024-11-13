package io.github.unjoinable.skyblock.user.sidebar;

import net.kyori.adventure.text.Component;
import net.minestom.server.scoreboard.Scoreboard;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SkyblockSidebar {

   @NotNull Component title();

   @NotNull List<Component> lines();

   void init();

   default @NotNull Scoreboard build() {
       Sidebar sidebar = new Sidebar(title());
       List<Component> lines = lines().reversed();

       for (int i = 0; i < lines.size(); i++) {
           Component line = lines.get(i);
           if (line == null) line = Component.empty();

           Sidebar.ScoreboardLine scoreboardLine = new Sidebar.ScoreboardLine(String.valueOf(i), line, i);
           sidebar.createLine(scoreboardLine);
       }
       return sidebar;
   }
}
