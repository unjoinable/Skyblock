package net.unjoinable.skyblock.ui.tab;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;

import java.util.*;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.unjoinable.skyblock.ui.tab.TabSkins.EMPTY_SLOT;
import static net.unjoinable.skyblock.ui.tab.TabSkins.HEADER_SLOT;

/**
 * Main Tab system class for managing player tab lists with advanced column layouts.
 * Supports multiple columns, custom headers, dynamic slots, and player sorting.
 */
public class Tab {
    private static final PlainTextComponentSerializer PLAIN_TEXT = PlainTextComponentSerializer.plainText();

    private static final EnumSet<PlayerInfoUpdatePacket.Action> FULL_UPDATE_ACTIONS = EnumSet.of(
            PlayerInfoUpdatePacket.Action.ADD_PLAYER,
            PlayerInfoUpdatePacket.Action.UPDATE_LISTED,
            PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER,
            PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME
    );

    private static final EnumSet<PlayerInfoUpdatePacket.Action> SLOT_UPDATE_ACTIONS = EnumSet.of(
            PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME
    );

    private Tab() {
        throw new AssertionError();
    }

    /**
     * Updates the tab list for a player based on the provided configuration.
     * @param viewer The player viewing the tab list
     * @param config Tab configuration defining layout and content
     */
    public static void updateTab(Player viewer, TabConfiguration config) {
        removeExistingPlayers(viewer, config);
        List<PlayerInfoUpdatePacket.Entry> entries = buildTabEntries(viewer, config);
        viewer.sendPacket(new PlayerInfoUpdatePacket(FULL_UPDATE_ACTIONS, entries.reversed()));
        viewer.sendPlayerListHeaderAndFooter(config.header(), config.footer());
    }

    /**
     * Updates a single slot in the tab list without refreshing the entire tab.
     * @param viewer The player viewing the tab list
     * @param config Tab configuration for bounds checking
     * @param column Column index (0-based)
     * @param row Row index (0-based)
     * @param slotConfig New slot configuration
     */
    public static void setSlot(Player viewer, TabConfiguration config, int column, int row, SlotConfig slotConfig) {
        validateSlotBounds(column, row, config.columns());

        int tabIndex = calculateTabIndex(column, row, config.columns());
        PlayerInfoUpdatePacket.Entry entry = createSlotEntry(viewer, tabIndex, slotConfig);

        viewer.sendPacket(new PlayerInfoUpdatePacket(SLOT_UPDATE_ACTIONS, List.of(entry)));
    }

    private static void removeExistingPlayers(Player viewer, TabConfiguration config) {
        List<UUID> playerUuids = config.players().stream()
                .map(Player::getUuid)
                .toList();
        viewer.sendPacket(new PlayerInfoRemovePacket(playerUuids));
    }

    private static List<PlayerInfoUpdatePacket.Entry> buildTabEntries(Player viewer, TabConfiguration config) {
        List<PlayerInfoUpdatePacket.Entry> entries = new ArrayList<>();
        List<Player> sortedPlayers = getSortedPlayers(config);
        Iterator<Player> playerIterator = sortedPlayers.iterator();

        for (int col = 0; col < config.columns(); col++) {
            ColumnConfig columnConfig = getColumnConfig(config, col);
            addColumnEntries(entries, viewer, columnConfig, col, config.columns(), playerIterator);
        }

        return entries;
    }

    private static List<Player> getSortedPlayers(TabConfiguration config) {
        return config.players().stream()
                .sorted(config.playerComparator())
                .toList();
    }

    private static ColumnConfig getColumnConfig(TabConfiguration config, int column) {
        return config.columnConfigs().getOrDefault(column, new ColumnConfig.Builder().build());
    }

    private static void addColumnEntries(List<PlayerInfoUpdatePacket.Entry> entries, Player viewer,
                                         ColumnConfig columnConfig, int column, int totalColumns,
                                         Iterator<Player> playerIterator) {
        for (int row = 0; row < 20; row++) {
            int tabIndex = calculateTabIndex(column, row, totalColumns);
            SlotConfig slotConfig = determineSlotConfig(columnConfig, row, playerIterator);
            entries.add(createSlotEntry(viewer, tabIndex, slotConfig));
        }
    }

    private static SlotConfig determineSlotConfig(ColumnConfig columnConfig, int row, Iterator<Player> playerIterator) {
        if (columnConfig.customSlots().containsKey(row)) {
            return columnConfig.customSlots().get(row);
        }

        if (row == 0 && columnConfig.hasHeader()) {
            assert columnConfig.headerText() != null;
            return SlotConfig.header(columnConfig.headerText());
        }

        if (shouldAddPlayer(columnConfig, row, playerIterator)) {
            return SlotConfig.player(playerIterator.next());
        }

        return SlotConfig.empty();
    }

    private static boolean shouldAddPlayer(ColumnConfig columnConfig, int row, Iterator<Player> playerIterator) {
        return columnConfig.allowPlayers() &&
                playerIterator.hasNext() &&
                !(row == 0 && columnConfig.hasHeader());
    }

    private static void validateSlotBounds(int column, int row, int totalColumns) {
        if (column < 0 || column >= totalColumns) {
            throw new IllegalArgumentException("Column index out of bounds: " + column);
        }
        if (row < 0 || row >= 20) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
    }

    /**
     * Calculates the tab index based on column and row positions.
     * Minecraft tab counts from bottom right, so conversion is needed.
     * @param column Column index
     * @param row Row index
     * @param totalColumns Total number of columns
     * @return Tab index for packet
     */
    private static int calculateTabIndex(int column, int row, int totalColumns) {
        return (totalColumns * 20) - (column * 20 + row + 1);
    }

    /**
     * Creates a packet entry for a slot based on its configuration.
     * @param viewer Player viewing the tab
     * @param tabIndex Tab index for the slot
     * @param slotConfig Slot configuration
     * @return Packet entry for the slot
     */
    private static PlayerInfoUpdatePacket.Entry createSlotEntry(Player viewer, int tabIndex, SlotConfig slotConfig) {
        UUID uuid = generateSlotUuid(viewer, tabIndex);
        assert slotConfig.player() != null;

        return switch (slotConfig.type()) {
            case HEADER, CUSTOM_TEXT -> createTextEntry(uuid, tabIndex, slotConfig.displayText());
            case PLAYER -> createPlayerEntry(uuid, tabIndex, slotConfig.player());
            default -> createEmptyEntry(uuid, tabIndex);
        };
    }

    private static UUID generateSlotUuid(Player viewer, int tabIndex) {
        return UUID.nameUUIDFromBytes(("tab-" + tabIndex + "-" + viewer.getUuid()).getBytes());
    }

    /**
     * Creates an empty slot entry for the tab list.
     */
    private static PlayerInfoUpdatePacket.Entry createEmptyEntry(UUID uuid, int tabIndex) {
        return new PlayerInfoUpdatePacket.Entry(
                uuid,
                "",
                EMPTY_SLOT,
                true,
                0,
                GameMode.SURVIVAL,
                Component.empty(),
                null,
                tabIndex
        );
    }

    /**
     * Creates a text slot entry for headers or custom text.
     */
    private static PlayerInfoUpdatePacket.Entry createTextEntry(UUID uuid, int tabIndex, Component displayText) {
        String username = serializeComponent(displayText);

        return new PlayerInfoUpdatePacket.Entry(
                uuid,
                username,
                HEADER_SLOT,
                true,
                0,
                GameMode.SURVIVAL,
                displayText,
                null,
                tabIndex
        );
    }

    /**
     * Creates a player slot entry with player skin and name.
     */
    private static PlayerInfoUpdatePacket.Entry createPlayerEntry(UUID uuid, int tabIndex, Player targetPlayer) {
        Component displayName = text(targetPlayer.getUsername(), WHITE);
        String username = serializeComponent(displayName);

        return new PlayerInfoUpdatePacket.Entry(
                uuid,
                username,
                createSkinProperties(targetPlayer),
                true,
                0,
                GameMode.SURVIVAL,
                displayName,
                null,
                tabIndex
        );
    }

    private static String serializeComponent(Component component) {
        return PLAIN_TEXT.serialize(component);
    }

    private static List<PlayerInfoUpdatePacket.Property> createSkinProperties(Player player) {
        if (player.getSkin() == null) {
            return List.of();
        }

        return List.of(new PlayerInfoUpdatePacket.Property(
                "textures",
                player.getSkin().textures(),
                player.getSkin().signature()
        ));
    }
}