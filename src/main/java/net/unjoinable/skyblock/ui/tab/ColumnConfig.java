package net.unjoinable.skyblock.ui.tab;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * Configuration for individual columns in the tab list.
 * @param headerText Optional header text (null if no header)
 * @param allowPlayers Whether players can be placed in this column
 * @param customSlots Map of row indices (0-19) to custom slot configurations
 */
public record ColumnConfig(
        @Nullable Component headerText,
        boolean allowPlayers,
        Map<Integer, SlotConfig> customSlots) {

    /**
     * Creates a new ColumnConfig with validated inputs.
     *
     * @param headerText Optional header text (null if no header)
     * @param allowPlayers Whether players can be placed in this column
     * @param customSlots Map of row indices to custom slot configurations
     * @throws IllegalArgumentException if any custom slot row is not between 0 and 19
     */
    public ColumnConfig {
        for (int row : customSlots.keySet()) {
            if (row < 0 || row >= 20) {
                throw new IllegalArgumentException("Custom slot row must be between 0 and 19, got: " + row);
            }
        }
        customSlots = Map.copyOf(customSlots);
    }

    /**
     * Returns whether this column has a header.
     *
     * @return true if headerText is not null, false otherwise
     */
    public boolean hasHeader() {
        return headerText != null;
    }

    /**
     * Creates a new builder for constructing ColumnConfig instances.
     *
     * @return A new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a simple ColumnConfig that allows players with no header or custom slots.
     *
     * @return A basic ColumnConfig for player columns
     */
    public static ColumnConfig forPlayers() {
        return new ColumnConfig(null, true, Map.of());
    }

    /**
     * Creates a ColumnConfig with only a header that allows players.
     *
     * @param headerText The header text to display
     * @return A ColumnConfig with the specified header
     */
    public static ColumnConfig withHeader(Component headerText) {
        return new ColumnConfig(headerText, true, Map.of());
    }

    /**
     * Builder class for creating ColumnConfig instances with a fluent API.
     *
     * <p>Provides a convenient way to construct ColumnConfig instances with
     * optional components while maintaining immutability of the final record.
     */
    public static class Builder {
        private Component headerText;
        private boolean allowPlayers = true;
        private final Map<Integer, SlotConfig> customSlots = new HashMap<>();

        /**
         * Sets the header text for this column.
         *
         * @param headerText Text to display in the header
         * @return This builder for method chaining
         */
        public Builder header(Component headerText) {
            this.headerText = headerText;
            return this;
        }

        /**
         * Sets whether players can be placed in this column.
         *
         * @param allowPlayers true to allow players, false to disallow
         * @return This builder for method chaining
         */
        public Builder allowPlayers(boolean allowPlayers) {
            this.allowPlayers = allowPlayers;
            return this;
        }

        /**
         * Adds a custom slot configuration for a specific row.
         *
         * @param row Row index (0-19 inclusive)
         * @param slotConfig Configuration for this slot
         * @return This builder for method chaining
         * @throws IllegalArgumentException if row is not between 0 and 19
         */
        public Builder customSlot(int row, SlotConfig slotConfig) {
            if (row < 0 || row >= 20) {
                throw new IllegalArgumentException("Row must be between 0 and 19, got: " + row);
            }
            this.customSlots.put(row, slotConfig);
            return this;
        }

        /**
         * Adds multiple custom slot configurations.
         *
         * @param slots Map of row indices to slot configurations
         * @return This builder for method chaining
         * @throws IllegalArgumentException if any row is not between 0 and 19
         */
        public Builder customSlots(Map<Integer, SlotConfig> slots) {
            for (Map.Entry<Integer, SlotConfig> entry : slots.entrySet()) {
                customSlot(entry.getKey(), entry.getValue());
            }
            return this;
        }

        /**
         * Builds the ColumnConfig instance.
         *
         * @return A new immutable ColumnConfig
         */
        public ColumnConfig build() {
            return new ColumnConfig(headerText, allowPlayers, customSlots);
        }
    }
}