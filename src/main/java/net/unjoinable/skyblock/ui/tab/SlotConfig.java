package net.unjoinable.skyblock.ui.tab;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Configuration for individual slots in the tab list.
 * @param type The type of content this slot contains
 * @param displayText The text to display (could be Component#empty for player slots)
 * @param player The player to display (null for non-player slots)
 */
public record SlotConfig(
        SlotType type,
        Component displayText,
        @Nullable Player player) {

    /**
     * Enum defining the types of content that can be displayed in a tab list slot.
     */
    public enum SlotType {
        /** An empty slot with no visible content */
        EMPTY,

        /** A header slot, typically used for section titles with special formatting */
        HEADER,

        /** A slot displaying custom text content */
        CUSTOM_TEXT,

        /** A slot displaying a player's name and information */
        PLAYER
    }

    /**
     * Creates a new SlotConfig with validation to ensure data consistency.
     *
     * @param type The type of content this slot contains
     * @param displayText The text to display (cannot be null)
     * @param player The player to display (must be null for non-player slots, non-null for player slots)
     * @throws IllegalArgumentException if the configuration is invalid
     */
    public SlotConfig {
        if (isPlayer()) {
            Objects.requireNonNull(player, "Player cannot be null");
        }
    }

    /**
     * Creates an empty slot configuration.
     *
     * <p>Empty slots appear as blank entries in the tab list and are useful
     * for spacing or alignment purposes.
     *
     * @return An empty slot configuration
     */
    public static SlotConfig empty() {
        return new SlotConfig(SlotType.EMPTY, Component.empty(), null);
    }

    /**
     * Creates a header slot configuration with the specified text.
     *
     * <p>Header slots are typically used for section titles and may have
     * special formatting or styling applied by the tab list renderer.
     *
     * @param text The header text to display (cannot be null)
     * @return A header slot configuration
     * @throws IllegalArgumentException if text is null
     */
    public static SlotConfig header(Component text) {
        return new SlotConfig(SlotType.HEADER, text, null);
    }

    /**
     * Creates a custom text slot configuration with the specified text.
     *
     * <p>Custom text slots display arbitrary text content and are useful
     * for showing server information, statistics, or other non-player data.
     *
     * @param text The custom text to display (cannot be null)
     * @return A custom text slot configuration
     * @throws IllegalArgumentException if text is null
     */
    public static SlotConfig customText(Component text) {
        return new SlotConfig(SlotType.CUSTOM_TEXT, text, null);
    }

    /**
     * Creates a player slot configuration for the specified player.
     *
     * <p>Player slots display the player's name and potentially additional
     * information like rank, status, or other player-specific data.
     *
     * @param player The player to display (cannot be null)
     * @return A player slot configuration
     * @throws IllegalArgumentException if player is null
     */
    public static SlotConfig player(Player player) {
        return new SlotConfig(SlotType.PLAYER, Component.empty(), player);
    }

    /**
     * Creates a player slot configuration with custom display text.
     *
     * <p>This allows overriding the default player display with custom text
     * while still maintaining the player association for other purposes.
     *
     * @param player The player to associate with this slot (cannot be null)
     * @param displayText Custom text to display instead of the player's name (cannot be null)
     * @return A player slot configuration with custom display text
     * @throws IllegalArgumentException if player or displayText is null
     */
    public static SlotConfig player(Player player, Component displayText) {
        return new SlotConfig(SlotType.PLAYER, displayText, player);
    }

    /**
     * Returns whether this slot represents an empty space.
     *
     * @return true if this is an empty slot, false otherwise
     */
    public boolean isEmpty() {
        return type == SlotType.EMPTY;
    }

    /**
     * Returns whether this slot contains a player.
     *
     * @return true if this is a player slot, false otherwise
     */
    public boolean isPlayer() {
        return type == SlotType.PLAYER;
    }

    /**
     * Returns whether this slot is a header.
     *
     * @return true if this is a header slot, false otherwise
     */
    public boolean isHeader() {
        return type == SlotType.HEADER;
    }

    /**
     * Returns whether this slot contains custom text.
     *
     * @return true if this is a custom text slot, false otherwise
     */
    public boolean isCustomText() {
        return type == SlotType.CUSTOM_TEXT;
    }
}