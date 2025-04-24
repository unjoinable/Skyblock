package net.skyblock.listeners;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.Material;
import net.skyblock.player.ItemSlot;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Optimized listener for player armor equipping events.
 */
public class PlayerUseItemListener implements EventListener<PlayerUseItemEvent> {

    // Direct material-to-slot mapping for O(1) lookups instead of iterating through sets
    private static final Map<Material, ItemSlot> ARMOR_SLOT_MAP = new ConcurrentHashMap<>();

    static {
        // Helmet mappings
        mapArmorToSlot(ItemSlot.HELMET,
                Material.LEATHER_HELMET,
                Material.CHAINMAIL_HELMET,
                Material.IRON_HELMET,
                Material.GOLDEN_HELMET,
                Material.DIAMOND_HELMET,
                Material.NETHERITE_HELMET,
                Material.TURTLE_HELMET);

        // Chestplate mappings
        mapArmorToSlot(ItemSlot.CHESTPLATE,
                Material.LEATHER_CHESTPLATE,
                Material.CHAINMAIL_CHESTPLATE,
                Material.IRON_CHESTPLATE,
                Material.GOLDEN_CHESTPLATE,
                Material.DIAMOND_CHESTPLATE,
                Material.NETHERITE_CHESTPLATE);

        // Leggings mappings
        mapArmorToSlot(ItemSlot.LEGGINGS,
                Material.LEATHER_LEGGINGS,
                Material.CHAINMAIL_LEGGINGS,
                Material.IRON_LEGGINGS,
                Material.GOLDEN_LEGGINGS,
                Material.DIAMOND_LEGGINGS,
                Material.NETHERITE_LEGGINGS);

        // Boots mappings
        mapArmorToSlot(ItemSlot.BOOTS,
                Material.LEATHER_BOOTS,
                Material.CHAINMAIL_BOOTS,
                Material.IRON_BOOTS,
                Material.GOLDEN_BOOTS,
                Material.DIAMOND_BOOTS,
                Material.NETHERITE_BOOTS);
    }

    /**
     * Helper method to map multiple armor materials to a slot
     */
    private static void mapArmorToSlot(ItemSlot slot, Material... materials) {
        for (Material material : materials) {
            ARMOR_SLOT_MAP.put(material, slot);
        }
    }

    @Override
    public @NotNull Class<PlayerUseItemEvent> eventType() {
        return PlayerUseItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerUseItemEvent event) {
        Material material = event.getItemStack().material();
        ItemSlot slot = ARMOR_SLOT_MAP.get(material);

        if (slot != null) {
            ((SkyblockPlayer) event.getPlayer()).getStatsManager().update(slot);
        }

        return Result.SUCCESS;
    }
}