package net.unjoinable.skyblock.event.listener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.item.Material;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.ui.inventory.ItemSlot;
import net.unjoinable.skyblock.player.ui.inventory.VanillaItemSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registers and handles global player-related events for the Skyblock server.
 * <p>
 * This class listens for events such as player spawning and initializes necessary
 * systems for the player upon joining the world. It also handles equipment changes
 * and inventory interactions to keep player statistics up to date.
 */
public class PlayerListener {
    private static final Map<Material, ItemSlot> ARMOR_SLOT_MAP = new ConcurrentHashMap<>();
    private final GlobalEventHandler eventHandler;

    static {
        // Helmet mappings
        mapArmorToSlot(VanillaItemSlot.HELMET,
                Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET,
                Material.DIAMOND_HELMET, Material.NETHERITE_HELMET, Material.TURTLE_HELMET);

        // Chestplate mappings
        mapArmorToSlot(VanillaItemSlot.CHESTPLATE,
                Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE,
                Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE);

        // Leggings mappings
        mapArmorToSlot(VanillaItemSlot.LEGGINGS,
                Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS,
                Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS);

        // Boots mappings
        mapArmorToSlot(VanillaItemSlot.BOOTS,
                Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS,
                Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS);
    }

    /**
     * Constructs a new {@code PlayerListener}
     *
     * @param eventHandler the global event handler used to register event listeners
     */
    public PlayerListener(@NotNull GlobalEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * Registers all player-related event listeners with the event handler.
     * <p>
     * This method must be called to activate all player event handling functionality.
     * It registers listeners for configuration, spawning, item swapping, inventory clicks,
     * and item usage events.
     */
    public void register() {
        registerPlayerSpawnListener();
        registerPlayerConfigListener();
        registerPlayerSwapItemListener();
        registerInventoryPreClickListener();
        registerPlayerUseItemEvent();
    }

    /**
     * Registers a listener for {@link AsyncPlayerConfigurationEvent}.
     * <p>
     * This is triggered during the player's configuration phase, before they
     * enter the game world. It initializes the {@link SkyblockPlayer} instance.
     */
    private void registerPlayerConfigListener() {
        InstanceContainer container = MinecraftServer.getInstanceManager().createInstanceContainer();
        container.setChunkLoader(new AnvilLoader("worlds/hub"));


        this.eventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(container);

            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            player.setRespawnPoint(new Pos(-2, 71, -68).withYaw(-180F));
            player.getSystemsManager().startAllSystems();
        });
    }

    /**
     * Registers a listener for {@link PlayerSpawnEvent}.
     * <p>
     * When a player spawns for the first time, this handler starts all systems associated
     * with that {@link SkyblockPlayer}.
     */
    private void registerPlayerSpawnListener() {
        this.eventHandler.addListener(PlayerSpawnEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            player.init();
        });
    }

    /**
     * Registers a listener for {@link PlayerSwapItemEvent}.
     * <p>
     * This event is triggered when a player swaps items between their main hand and off-hand.
     * Updates the player's statistics for the main hand slot to reflect the newly equipped item.
     */
    private void registerPlayerSwapItemListener() {
        this.eventHandler.addListener(PlayerSwapItemEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

            player.getStatSystem().updateSlot(VanillaItemSlot.MAIN_HAND);
        });
    }

    /**
     * Registers a listener for {@link InventoryPreClickEvent}.
     * <p>
     * This event fires before an inventory click is processed. It detects clicks on
     * armor slots or the main hand slot and schedules a stat system update for the
     * affected slot at the end of the current tick to ensure the inventory change
     * has been fully processed.
     */
    private void registerInventoryPreClickListener() {
        this.eventHandler.addListener(InventoryPreClickEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

            int clickedSlot = event.getSlot();
            ItemSlot itemSlot = getItemSlot(clickedSlot, player);

            if (itemSlot != null) {
                MinecraftServer.getSchedulerManager()
                        .scheduleEndOfTick(() -> player.getStatSystem().updateSlot(itemSlot));
            }
        });
    }

    /**
     * Registers a listener for {@link PlayerUseItemEvent}.
     * <p>
     * This event is triggered when a player uses (right-clicks) an item. If the item
     * is armor, it schedules a stat system update for the corresponding armor slot
     * at the end of the tick, allowing the armor to be properly equipped and stats updated.
     */
    private void registerPlayerUseItemEvent() {
        this.eventHandler.addListener(PlayerUseItemEvent.class, event -> {
            Material material = event.getItemStack().material();
            ItemSlot slot = ARMOR_SLOT_MAP.get(material);

            if (slot != null) {
                SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
                MinecraftServer.getSchedulerManager().scheduleEndOfTick(() -> player.getStatSystem().updateSlot(slot));
            }
        });
    }

    // Utilities

    /**
     * Determines which equipment slot corresponds to a clicked inventory slot.
     * <p>
     * Maps specific inventory slot indices to their corresponding equipment slots:
     * <ul>
     *   <li>Slot 41: Helmet slot</li>
     *   <li>Slot 42: Chestplate slot</li>
     *   <li>Slot 43: Leggings slot</li>
     *   <li>Slot 44: Boots slot</li>
     *   <li>Player's held slot: Main hand slot</li>
     * </ul>
     *
     * @param clickedSlot the inventory slot index that was clicked
     * @param player the player whose inventory was clicked
     * @return the corresponding {@link ItemSlot} if the clicked slot is an equipment slot,
     *         null otherwise
     */
    private static @Nullable ItemSlot getItemSlot(int clickedSlot, SkyblockPlayer player) {
        ItemSlot itemSlot = null;

        switch (clickedSlot) {
            case 41 -> itemSlot = VanillaItemSlot.HELMET;
            case 42 -> itemSlot = VanillaItemSlot.CHESTPLATE;
            case 43 -> itemSlot = VanillaItemSlot.LEGGINGS;
            case 44 -> itemSlot = VanillaItemSlot.BOOTS;
            default -> {
                if (clickedSlot == player.getHeldSlot()) {
                    itemSlot = VanillaItemSlot.MAIN_HAND;
                }
            }
        }
        return itemSlot;
    }

    /**
     * Helper method to map multiple armor materials to a specific equipment slot.
     * <p>
     * This method populates the {@link #ARMOR_SLOT_MAP} with mappings from armor materials
     * to their corresponding equipment slots, enabling quick lookups when armor is equipped.
     *
     * @param slot the equipment slot that the materials should map to
     * @param materials the armor materials that belong in this slot
     */
    private static void mapArmorToSlot(ItemSlot slot, Material... materials) {
        for (Material material : materials) {
            ARMOR_SLOT_MAP.put(material, slot);
        }
    }
}