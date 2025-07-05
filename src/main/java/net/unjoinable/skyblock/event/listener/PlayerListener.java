package net.unjoinable.skyblock.event.listener;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.level.IslandManager;
import net.unjoinable.skyblock.level.SkyblockIsland;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;
import net.unjoinable.skyblock.player.ui.inventory.ItemSlot;
import net.unjoinable.skyblock.player.ui.inventory.VanillaItemSlot;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

/**
 * Handles player events including spawning, chat, inventory interactions, and item usage.
 * Manages player initialization, stat updates, and ability execution.
 */
public class PlayerListener {
    private static final Tag<Boolean> IGNORE_ANIMATION = Tag.Boolean("leftclick:dropping");
    private static final Tag<Boolean> IS_DIGGING = Tag.Boolean("player:isdigging");
    private static final Map<Material, ItemSlot> ARMOR_SLOT_MAP = new ConcurrentHashMap<>();
    private final IslandManager islandManager;
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
     * Creates a new PlayerListener.
     *
     * @param islandManager manages island instances for player spawning
     * @param eventHandler the global event handler for registering listeners
     */
    public PlayerListener(IslandManager islandManager, GlobalEventHandler eventHandler) {
        this.islandManager = islandManager;
        this.eventHandler = eventHandler;
    }

    /**
     * Registers all player event listeners.
     * Must be called to activate player event handling.
     */
    public void register() {
        registerPlayerSpawnListener();
        registerPlayerConfigListener();
        registerPlayerSwapItemListener();
        registerInventoryPreClickListener();
        registerPlayerUseItemEvent();
        registerPlayerYapListener();
        registerPlayerChangeHeldSlotListener();
        registerPlayerHandAnimationListener();
        registerItemDropEvent();
        registerPlayerBlockInteractEvent();
        registerInventoryClickEvent();
        registerPlayerEntityInteractEvent();
        registerPlayerLeftClickEvent();
        registerStartDiggingEvent();
        registerStopDiggingEvent();
        registerFinishDiggingEvent();
        registerBeginUseItemEvent();
    }

    private void registerBeginUseItemEvent() {
        this.eventHandler.addListener(PlayerBeginItemUseEvent.class, event -> {
            if (event.getItemStack().material() == Material.BOW) {
                event.setCancelled(true);
            }
        } );
    }

    private void registerFinishDiggingEvent() {
        this.eventHandler.addListener(PlayerFinishDiggingEvent.class, event -> event.getPlayer().removeTag(IS_DIGGING));
    }

    private void registerStopDiggingEvent() {
        this.eventHandler.addListener(PlayerCancelDiggingEvent.class, event -> event.getPlayer().removeTag(IS_DIGGING));
    }

    private void registerStartDiggingEvent() {
        this.eventHandler.addListener(PlayerStartDiggingEvent.class, event -> event.getPlayer().setTag(IS_DIGGING, true));
    }

    private void registerPlayerLeftClickEvent() {
        this.eventHandler.addListener(PlayerLeftClickEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            player.getAbilitySystem().tryUse(event.getItemStack(), ExecutionType.LEFT_CLICK);
        });
    }

    private void registerPlayerEntityInteractEvent() {
        this.eventHandler.addListener(PlayerEntityInteractEvent.class, event -> event.getPlayer().setTag(IGNORE_ANIMATION, true));
    }

    private void registerInventoryClickEvent() {
        this.eventHandler.addListener(InventoryClickEvent.class, event -> {
            if (event.getClickType() == ClickType.DROP) {
                event.getPlayer().setTag(IGNORE_ANIMATION, true);
            }
        });
    }

    private void registerPlayerBlockInteractEvent() {
        this.eventHandler.addListener(PlayerBlockInteractEvent.class, event -> event.getPlayer().setTag(IGNORE_ANIMATION, true));
    }

    private void registerItemDropEvent() {
        this.eventHandler.addListener(ItemDropEvent.class, event -> event.getPlayer().setTag(IGNORE_ANIMATION, true));
    }

    private void registerPlayerHandAnimationListener() {
        this.eventHandler.addListener(PlayerHandAnimationEvent.class, event -> {
            SkyblockPlayer player = ((SkyblockPlayer) event.getPlayer());

            if (player.hasTag(IGNORE_ANIMATION)) {
                player.removeTag(IGNORE_ANIMATION);
                return;
            }

            if (player.hasTag(IS_DIGGING)) return;

            if (event.getHand() == PlayerHand.MAIN) {
                eventHandler.call(new PlayerLeftClickEvent(player, player.getItemInMainHand()));
            }
        });
    }

    private void registerPlayerChangeHeldSlotListener() {
        this.eventHandler.addListener(PlayerChangeHeldSlotEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

            MinecraftServer.getSchedulerManager()
                    .scheduleEndOfTick(() -> player.getStatSystem().updateSlot(VanillaItemSlot.MAIN_HAND));
        });
    }

    private void registerPlayerYapListener() {
        this.eventHandler.addListener(PlayerChatEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            PlayerRank rank = player.getPlayerRank();

            Component message = rank == PlayerRank.DEFAULT
                    ? text(player.getUsername() + ": " + event.getRawMessage(), GRAY)
                    : rank.getComponentPrefix()
                    .append(text(" " + player.getUsername(), rank.getColor()))
                    .append(text(": " + event.getRawMessage(), WHITE));

            event.setFormattedMessage(message);
        });
    }

    /**
     * Sets up player spawning instance and initializes player systems.
     */
    private void registerPlayerConfigListener() {
        Instance instance = islandManager.getInstance(SkyblockIsland.HUB);

        this.eventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);

            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            player.setRespawnPoint(SkyblockIsland.HUB.spawnPoint());
            player.getSystemsManager().startAllSystems();
        });
    }

    /**
     * Initializes player on first spawn.
     */
    private void registerPlayerSpawnListener() {
        this.eventHandler.addListener(PlayerSpawnEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            if (event.isFirstSpawn()) player.init();
        });
    }

    /**
     * Updates main hand stats when player swaps items.
     */
    private void registerPlayerSwapItemListener() {
        this.eventHandler.addListener(PlayerSwapItemEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

            player.getStatSystem().updateSlot(VanillaItemSlot.MAIN_HAND);
        });
    }

    /**
     * Updates equipment stats when player clicks armor or main hand slots.
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
     * Handles armor equipping and ability usage on right-click.
     */
    private void registerPlayerUseItemEvent() {
        this.eventHandler.addListener(PlayerUseItemEvent.class, event -> {
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            ItemStack itemStack = event.getItemStack();
            Material material = itemStack.material();
            ItemSlot slot = ARMOR_SLOT_MAP.get(material);

            if (slot != null) {
                MinecraftServer.getSchedulerManager().scheduleEndOfTick(() -> player.getStatSystem().updateSlot(slot));
                return;
            }

            player.getAbilitySystem().tryUse(itemStack, ExecutionType.RIGHT_CLICK);
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