package com.github.unjoinable.skyblock.user.sidebar;

import net.kyori.adventure.text.Component;
import net.minestom.server.scoreboard.Scoreboard;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a Skyblock sidebar interface.
 */
public interface SkyblockSidebar {

   /**
    * Returns the title of the sidebar.
    *
    * @return the title of the sidebar
    */
   @NotNull Component title();

   /**
    * Returns the lines of the sidebar.
    *
    * @return the lines of the sidebar
    */
   @NotNull List<Component> lines();

   /**
    * Initializes the sidebar.
    */
   void init();

   /**
    * Builds and returns a Scoreboard instance based on the sidebar's title and lines.
    *
    * @return the Scoreboard instance
    */
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
