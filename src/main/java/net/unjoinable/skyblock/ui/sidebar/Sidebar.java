package net.unjoinable.skyblock.ui.sidebar;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Enhanced sidebar implementation for Skyblock that supports both static and dynamic lines.
 * Dynamic lines can update their content based on player data, while static lines remain constant.
 */
public class Sidebar {
    private final SkyblockPlayer player;
    private final net.minestom.server.scoreboard.Sidebar sidebar;
    private final List<SidebarLine> lines;

    /**
     * Creates a new SkyblockSidebar with the specified title.
     *
     * @param title The title component to display at the top of the sidebar
     */
    public Sidebar(SkyblockPlayer player, Component title) {
        this.player = player;
        this.sidebar = new net.minestom.server.scoreboard.Sidebar(title);
        this.lines = new ArrayList<>();
    }

    /**
     * Adds a static line that never changes its content.
     *
     * @param component The static component to display
     */
    public void addStaticLine(Component component) {
        lines.add(new SidebarLine(component, null));
    }

    /**
     * Adds a dynamic line that updates its content based on player data.
     * The function will be called each time the sidebar is updated for a player.
     *
     * @param lineFunction Function that takes a SkyblockPlayer and returns the component to display
     */
    public void addDynamicLine(Function<SkyblockPlayer, Component> lineFunction) {
        lines.add(new SidebarLine(null, lineFunction));
    }

    public void addEmptyLine() {
        addStaticLine(Component.empty());
    }

    /**
     * Clears all lines from the sidebar.
     */
    public void clearLines() {
        lines.clear();
        sidebar.getLines().forEach(line -> sidebar.removeLine(line.getId()));
        fullUpdate();
    }

    /**
     * Updates the sidebar content for a specific player.
     * This method evaluates all dynamic lines and refreshes the display.
     */
    public void update() {
        for (int i = 0; i < lines.size(); i++) {
            SidebarLine line = lines.get(i);

            if (line.isDynamic()) {
                String lineId = generateLineId(i);
                sidebar.updateLineContent(lineId, line.getComponent(player));
            }
        }
    }

    public void fullUpdate() {
        sidebar.getLines().forEach(line -> sidebar.removeLine(line.getId()));

        for (int i = 0; i < lines.size(); i++) {
            SidebarLine sidebarLine = lines.get(i);
            Component component = sidebarLine.getComponent(player);

            String lineId = generateLineId(i);

            net.minestom.server.scoreboard.Sidebar.ScoreboardLine line = new net.minestom.server.scoreboard.Sidebar.ScoreboardLine(
                    lineId,
                    component,
                    lines.size() - i,
                    net.minestom.server.scoreboard.Sidebar.NumberFormat.blank()
            );
            sidebar.createLine(line);
        }
    }

    /**
     * Sends the sidebar to a player and performs initial update.
     */
    public void send() {
        fullUpdate();
        sidebar.addViewer(player);
    }

    /**
     * Generates a unique line ID for the scoreboard.
     * Uses alphabetic characters, then falls back to numeric IDs for large sidebars.
     *
     * @param index The line index
     * @return A unique string ID for the line
     */
    private String generateLineId(int index) {
        return "line_" + index;
    }

    /**
     * Internal record representing a sidebar line that can be either static or dynamic.
     *
     * @param staticComponent Static component (null if dynamic)
     * @param dynamicFunction Dynamic function (null if static)
     */
    private record SidebarLine (
            @Nullable Component staticComponent,
            @Nullable Function<SkyblockPlayer, Component> dynamicFunction
    ) {

        /**
         * Compact constructor with validation to ensure exactly one component type is provided.
         */
        public SidebarLine {
            if (staticComponent == null && dynamicFunction == null) {
                throw new IllegalArgumentException("Either staticComponent or dynamicFunction must be non-null");
            }
            if (staticComponent != null && dynamicFunction != null) {
                throw new IllegalArgumentException("Cannot have both static and dynamic components");
            }
        }

        /**
         * Gets the component for this line, either static or by evaluating the dynamic function.
         *
         * @param player The player context for dynamic evaluation
         * @return The component to display
         */
        Component getComponent(SkyblockPlayer player) {
            assert staticComponent != null || dynamicFunction != null;
            return staticComponent != null ? staticComponent : dynamicFunction.apply(player);
        }

        /**
         * Checks if this line is dynamic (updates based on player data).
         *
         * @return true if dynamic, false if static
         */
        boolean isDynamic() {
            return dynamicFunction != null;
        }
    }
}