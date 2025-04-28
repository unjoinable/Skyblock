package net.skyblock.ui.gui;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.skyblock.player.SkyblockPlayer;
import net.skyblock.stats.StatProfile;
import net.skyblock.stats.Statistic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main menu GUI in Hypixel Skyblock.
 * Displays player stats and provides navigation to other game features.
 */
public class SkyblockMainMenu extends SkyblockInventory {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.#");
    private static final ItemStack MENU_GLASS = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
            .customName(Component.text(" "))
            .build();

    // Cache common menu items for performance
    private static final Int2ObjectMap<ItemStack> STATIC_ITEMS = new Int2ObjectOpenHashMap<>();

    static {
        // Initialize static menu items
        STATIC_ITEMS.put(48, createMenuItem(Material.NETHER_STAR, "SkyBlock Menu", NamedTextColor.GREEN,
                List.of("§7View all of SkyBlock's features in one menu!")));

        STATIC_ITEMS.put(49, createMenuItem(Material.PLAYER_HEAD, "Your Profile", NamedTextColor.AQUA,
                List.of("§7View your SkyBlock stats, collections,", "§7skills, and more!")));

        STATIC_ITEMS.put(50, createMenuItem(Material.CHEST, "Storage", NamedTextColor.YELLOW,
                List.of("§7Access your storage, wardrobe,", "§7and ender chest!")));

        STATIC_ITEMS.put(51, createMenuItem(Material.CRAFTING_TABLE, "Crafting Table", NamedTextColor.RED,
                List.of("§7Open a crafting table to create", "§7items and equipment!")));

        STATIC_ITEMS.put(52, createMenuItem(Material.GOLD_INGOT, "Bazaar", NamedTextColor.GOLD,
                List.of("§7Buy and sell Skyblock items with", "§7other players through buy and sell", "§7orders!")));

        STATIC_ITEMS.put(53, createMenuItem(Material.EMERALD, "Auction House", NamedTextColor.AQUA,
                List.of("§7Buy from and sell items to", "§7other players!")));
    }

    /**
     * Creates the main menu for a Skyblock player.
     *
     * @param player The player to create the menu for
     * @return The created menu instance
     */
    public static SkyblockMainMenu create(SkyblockPlayer player) {
        SkyblockMainMenu menu = new SkyblockMainMenu(player);
        menu.init();
        return menu;
    }

    private final SkyblockPlayer player;
    private final StatProfile statProfile;

    /**
     * Creates a new Skyblock main menu.
     *
     * @param player The player this menu belongs to
     */
    private SkyblockMainMenu(SkyblockPlayer player) {
        super(InventoryType.CHEST_6_ROW, "SkyBlock Menu");
        this.player = player;
        this.statProfile = player.getStatProfile();
        setCancelAllClicks(true);
    }

    /**
     * Initializes the menu content.
     */
    private void init() {
        // Fill the background
        fill(MENU_GLASS);

        // Set the stats display items
        setPlayerInfoItems();
        setStatItems();

        // Set the navigation items
        setNavigationItems();
    }

    /**
     * Sets the player information items in the menu.
     */
    private void setPlayerInfoItems() {
        // Player head with name and rank
        ItemStack playerHead = ItemStack.builder(Material.PLAYER_HEAD)
                .customName(Component.text(player.getUsername(), NamedTextColor.AQUA, TextDecoration.BOLD))
                .lore(List.of(
                        Component.text("Rank: ", NamedTextColor.GRAY).append(Component.text("MVP+", NamedTextColor.AQUA)),
                        Component.text("Coins: ", NamedTextColor.GRAY).append(Component.text(formatNumber(1250000), NamedTextColor.GOLD)),
                        Component.text("Purse: ", NamedTextColor.GRAY).append(Component.text(formatNumber(350000), NamedTextColor.GOLD))
                ))
                .build();

        setItem(4, playerHead);
    }

    /**
     * Sets the stat display items in the menu.
     */
    private void setStatItems() {
        // Health stats
        setItem(19, createStatItem(Material.RED_DYE, "Health", NamedTextColor.RED, Statistic.HEALTH,
                List.of("§7Health is your life force.", "§7When you run out of Health,", "§7you die.")));

        // Defense stats
        setItem(20, createStatItem(Material.IRON_CHESTPLATE, "Defense", NamedTextColor.GREEN, Statistic.DEFENSE,
                List.of("§7Defense reduces the damage that", "§7you take from enemies.")));

        // Strength stats
        setItem(21, createStatItem(Material.BLAZE_POWDER, "Strength", NamedTextColor.RED, Statistic.STRENGTH,
                List.of("§7Strength increases your melee", "§7damage.")));

        // Speed stats
        setItem(22, createStatItem(Material.SUGAR, "Speed", NamedTextColor.WHITE, Statistic.SPEED,
                List.of("§7Speed increases your walk speed.", "§7Base speed is 100.")));

        // Crit Chance stats
        setItem(23, createStatItem(Material.DIAMOND_SWORD, "Crit Chance", NamedTextColor.BLUE, Statistic.CRIT_CHANCE,
                List.of("§7Crit Chance is your chance to", "§7deal extra damage on enemies.")));

        // Crit Damage stats
        setItem(24, createStatItem(Material.GOLDEN_SWORD, "Crit Damage", NamedTextColor.BLUE, Statistic.CRIT_DAMAGE,
                List.of("§7Crit Damage is the amount of", "§7extra damage you deal when", "§7landing a critical hit.")));

        // Intelligence stats
        setItem(25, createStatItem(Material.ENCHANTED_BOOK, "Intelligence", NamedTextColor.AQUA, Statistic.INTELLIGENCE,
                List.of("§7Intelligence increases your", "§7Mana and Ability Damage.")));

        // Mining Speed
        setItem(28, createStatItem(Material.IRON_PICKAXE, "Mining Speed", NamedTextColor.GOLD, Statistic.MINING_SPEED,
                List.of("§7Mining Speed increases the", "§7speed at which you break blocks.")));

        // Magic Find
        setItem(29, createStatItem(Material.NETHER_STAR, "Magic Find", NamedTextColor.AQUA, Statistic.MAGIC_FIND,
                List.of("§7Magic Find increases your chance", "§7to find rare items.")));

        // Bonus Attack Speed
        setItem(30, createStatItem(Material.FEATHER, "Attack Speed", NamedTextColor.YELLOW, Statistic.BONUS_ATTACK_SPEED,
                List.of("§7Bonus Attack Speed decreases the", "§7cooldown between your attacks.")));

        // Sea Creature Chance
        setItem(31, createStatItem(Material.FISHING_ROD, "Sea Creature Chance", NamedTextColor.AQUA, Statistic.SEA_CREATURE_CHANCE,
                List.of("§7Sea Creature Chance increases", "§7your chance to catch sea creatures", "§7while fishing.")));

        // Pet Luck
        setItem(32, createStatItem(Material.BONE, "Pet Luck", NamedTextColor.LIGHT_PURPLE, Statistic.PET_LUCK,
                List.of("§7Pet Luck increases your chance", "§7to get higher rarity pets.")));
    }

    /**
     * Sets the navigation items at the bottom of the menu.
     */
    private void setNavigationItems() {
        // Set static items from cache
        for (int slot : STATIC_ITEMS.keySet()) {
            setItem(slot, STATIC_ITEMS.get(slot), this::handleNavigationClick);
        }
    }

    /**
     * Handles clicks on navigation items.
     *
     * @param player The player who clicked
     * @param inventory The inventory that was clicked
     */
    private void handleNavigationClick(SkyblockPlayer player, SkyblockInventory inventory) {
        // Close current inventory and send a message (in a real implementation, this would open other menus)
        player.closeInventory();
        player.sendMessage(Component.text("This feature would be implemented in a full Skyblock server!", NamedTextColor.YELLOW));
    }

    /**
     * Creates an item stack for displaying a statistic.
     *
     * @param material The material to use
     * @param name The display name of the stat
     * @param color The color of the name
     * @param stat The statistic to display
     * @param description The description of the stat
     * @return The created item stack
     */
    private ItemStack createStatItem(Material material, String name, NamedTextColor color, Statistic stat, List<String> description) {
        double value = statProfile.get(stat);

        List<Component> lore = new ArrayList<>();
        for (String line : description) {
            lore.add(Component.text(line));
        }
        lore.add(Component.empty());
        lore.add(Component.text("§7Current: ")
                .append(Component.text(formatNumber(value), color)));

        return ItemStack.builder(material)
                .customName(Component.text(name, color, TextDecoration.BOLD))
                .lore(lore)
                .build();
    }

    /**
     * Creates a menu item with the specified properties.
     *
     * @param material The item material
     * @param name The display name
     * @param color The name color
     * @param loreLines The item lore
     * @return The created item stack
     */
    private static ItemStack createMenuItem(Material material, String name, NamedTextColor color, List<String> loreLines) {
        List<Component> lore = new ArrayList<>();
        for (String line : loreLines) {
            lore.add(Component.text(line));
        }

        return ItemStack.builder(material)
                .customName(Component.text(name, color, TextDecoration.BOLD))
                .lore(lore)
                .build();
    }

    /**
     * Formats a number with commas for thousands and limits decimal places.
     *
     * @param number The number to format
     * @return The formatted string
     */
    private static String formatNumber(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    /**
     * Opens this menu for the specified player.
     */
    @Override
    public void open(SkyblockPlayer player) {
        super.open(player);
    }
}