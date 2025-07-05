package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.unjoinable.skyblock.player.ui.inventory.ItemSlot;
import net.unjoinable.skyblock.player.ui.inventory.VanillaItemSlot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared constants for player event listeners.
 */
public class PlayerListenerConstants {
    public static final Tag<Boolean> IGNORE_ANIMATION = Tag.Boolean("leftclick:dropping");
    public static final Tag<Boolean> IS_DIGGING = Tag.Boolean("player:isdigging");
    public static final Map<Material, ItemSlot> ARMOR_SLOT_MAP = new ConcurrentHashMap<>();

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

    private static void mapArmorToSlot(ItemSlot slot, Material... materials) {
        for (Material material : materials) {
            ARMOR_SLOT_MAP.put(material, slot);
        }
    }
}