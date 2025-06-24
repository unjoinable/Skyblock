package net.unjoinable.ui.sidebar;

import net.kyori.adventure.text.Component;
import net.minestom.server.scoreboard.Sidebar;
import net.unjoinable.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Enhanced sidebar implementation for Skyblock that supports both static and dynamic lines.
 * Dynamic lines can update their content based on player data, while static lines remain constant.
 */
public class SkyblockSidebar {
    private final Sidebar sidebar;
    private final List<SidebarLine> lines;

    /**
     * Creates a new SkyblockSidebar with the specified title.
     *
     * @param title The title component to display at the top of the sidebar
     * @throws NullPointerException if title is null
     */
    public SkyblockSidebar(@NotNull Component title) {
        this.sidebar = new Sidebar(title);
        this.lines = new ArrayList<>();
    }

    /**
     * Adds a static line that never changes its content.
     *
     * @param component The static component to display
     * @throws NullPointerException if component is null
     */
    public void addStaticLine(@NotNull Component component) {
        lines.add(new SidebarLine(component, null));
    }

    /**
     * Adds a dynamic line that updates its content based on player data.
     * The function will be called each time the sidebar is updated for a player.
     *
     * @param lineFunction Function that takes a SkyblockPlayer and returns the component to display
     * @throws NullPointerException if lineFunction is null
     */
    public void addDynamicLine(@NotNull Function<SkyblockPlayer, Component> lineFunction) {
        lines.add(new SidebarLine(null, lineFunction));
    }

    /**
     * Adds multiple static lines at once.
     *
     * @param components List of static components to add
     * @throws NullPointerException if components is null or contains null elements
     */
    public void addStaticLines(@NotNull List<Component> components) {
        for (Component component : components) {
            addStaticLine(component);
        }
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
    }

    /**
     * Updates the sidebar content for a specific player.
     * This method evaluates all dynamic lines and refreshes the display.
     *
     * @param player The player whose sidebar should be updated
     * @throws NullPointerException if player is null
     */
    public void update(@NotNull SkyblockPlayer player) {
        for (int i = 0; i < lines.size(); i++) {
            SidebarLine line = lines.get(i);

            if (line.isDynamic()) {
                String lineId = generateLineId(i);
                sidebar.updateLineContent(lineId, line.getComponent(player));
            }
        }
    }

    public void fullUpdate(@NotNull SkyblockPlayer player) {
        sidebar.getLines().forEach(line -> sidebar.removeLine(line.getId()));

        for (int i = 0; i < lines.size(); i++) {
            SidebarLine sidebarLine = lines.get(i);
            Component component = sidebarLine.getComponent(player);

            String lineId = generateLineId(i);

            Sidebar.ScoreboardLine line = new Sidebar.ScoreboardLine(
                    lineId,
                    component,
                    lines.size() - i,
                    Sidebar.NumberFormat.blank()
            );
            sidebar.createLine(line);
        }
    }

    /**
     * Sends the sidebar to a player and performs initial update.
     *
     * @param player The player to send the sidebar to
     * @throws NullPointerException if player is null
     */
    public void send(@NotNull SkyblockPlayer player) {
        fullUpdate(player);
        sidebar.addViewer(player);
    }

    /**
     * Removes the sidebar from a player.
     *
     * @param player The player to remove the sidebar from
     * @throws NullPointerException if player is null
     */
    public void remove(@NotNull SkyblockPlayer player) {
        sidebar.removeViewer(player);
    }

    /**
     * Gets the underlying Minestom Sidebar instance.
     * Use with caution as direct modifications may interfere with this wrapper's functionality.
     *
     * @return The underlying sidebar
     */
    public @NotNull Sidebar getSidebar() {
        return sidebar;
    }

    /**
     * Gets the number of lines currently in the sidebar.
     *
     * @return The number of lines
     */
    public int getLineCount() {
        return lines.size();
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