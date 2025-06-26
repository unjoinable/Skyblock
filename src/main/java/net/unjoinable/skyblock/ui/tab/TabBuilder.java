package net.unjoinable.skyblock.ui.tab;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.util.Collection;
import java.util.Comparator;

/**
 * Builder class providing convenient methods for creating and updating tab configurations.
 * Offers simplified methods for common tab layouts and configurations.
 */
public class TabBuilder {
    private final TabConfiguration.Builder configBuilder;

    /**
     * Creates a new TabBuilder instance.
     */
    public TabBuilder() {
        this.configBuilder = new TabConfiguration.Builder();
    }

    /**
     * Sets the number of columns for the tab list.
     * @param columns Number of columns (1-4)
     * @return This builder
     */
    public TabBuilder columns(int columns) {
        configBuilder.columns(columns);
        return this;
    }

    /**
     * Sets the collection of players to display.
     * @param players Collection of players
     * @return This builder
     */
    public TabBuilder players(Collection<Player> players) {
        configBuilder.players(players);
        return this;
    }

    /**
     * Sets the comparator for sorting players.
     * @param comparator Player comparator
     * @return This builder
     */
    public TabBuilder playerComparator(Comparator<Player> comparator) {
        configBuilder.playerComparator(comparator);
        return this;
    }

    /**
     * Configures a column with a custom ColumnConfig.
     * @param index Column index
     * @param config Column configuration
     * @return This builder
     */
    public TabBuilder column(int index, ColumnConfig config) {
        configBuilder.configureColumn(index, config);
        return this;
    }

    /**
     * Creates a simple player column without a header.
     * @param index Column index
     * @return This builder
     */
    public TabBuilder playersColumn(int index) {
        configBuilder.configureColumn(index, new ColumnConfig.Builder().allowPlayers(true).build());
        return this;
    }

    /**
     * Creates a player column with a header.
     * @param index Column index
     * @param headerText Header text to display
     * @return This builder
     */
    public TabBuilder headerColumn(int index, Component headerText) {
        configBuilder.configureColumn(index, new ColumnConfig.Builder()
                .header(headerText)
                .allowPlayers(true)
                .build());
        return this;
    }

    /**
     * Creates an info-only column with a header that doesn't allow players.
     * @param index Column index
     * @param headerText Header text to display
     * @return This builder
     */
    public TabBuilder infoColumn(int index, Component headerText) {
        configBuilder.configureColumn(index, new ColumnConfig.Builder()
                .header(headerText)
                .allowPlayers(false)
                .build());
        return this;
    }

    /**
     * Builds the TabConfiguration.
     * @return New TabConfiguration
     */
    public TabConfiguration build() {
        return configBuilder.build();
    }

    /**
     * Builds the configuration and immediately updates the tab for the specified player.
     * @param viewer Player to update the tab for
     */
    public void updateFor(Player viewer) {
        Tab.updateTab(viewer, build());
    }
}