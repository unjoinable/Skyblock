package net.unjoinable.skyblock.ui.tab;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.util.*;

public record TabConfiguration(
        int columns,
        Map<Integer, ColumnConfig> columnConfigs,
        Collection<Player> players,
        Comparator<Player> playerComparator,
        Component header,
        Component footer) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int columns = 1;
        private final Map<Integer, ColumnConfig> columnConfigs = new HashMap<>();
        private Collection<Player> players = new ArrayList<>();
        private Comparator<Player> playerComparator = Comparator.comparing(Player::getUsername);
        private Component header = Component.empty();
        private Component footer = Component.empty();

        public Builder header(Component header) {
            this.header = header;
            return this;
        }

        public Builder footer(Component footer) {
            this.footer = footer;
            return this;
        }

        public Builder columns(int columns) {
            if (columns < 1 || columns > 4) {
                throw new IllegalArgumentException("Columns must be between 1 and 4");
            }
            this.columns = columns;
            return this;
        }

        public Builder configureColumn(int column, ColumnConfig config) {
            if (column < 0 || column >= columns) {
                throw new IllegalArgumentException("Column index out of bounds: " + column);
            }
            this.columnConfigs.put(column, config);
            return this;
        }

        public Builder players(Collection<Player> players) {
            this.players = players;
            return this;
        }

        public Builder playerComparator(Comparator<Player> comparator) {
            this.playerComparator = comparator;
            return this;
        }

        public TabConfiguration build() {
            return new TabConfiguration(columns, new HashMap<>(columnConfigs), players, playerComparator, header, footer);
        }
    }
}
