package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.ui.inventory.ItemSlot;
import net.unjoinable.skyblock.player.ui.inventory.VanillaItemSlot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Handles player item use events, armor equipping, and ability usage.
 */
public class PlayerUseItemListener implements Consumer<PlayerUseItemEvent> {
    private static final Map<Material, ItemSlot> ARMOR_SLOT_MAP = new ConcurrentHashMap<>();

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

    @Override
    public void accept(PlayerUseItemEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        ItemStack itemStack = event.getItemStack();
        Material material = itemStack.material();
        ItemSlot slot = ARMOR_SLOT_MAP.get(material);

        if (slot != null) {
            MinecraftServer.getSchedulerManager().scheduleEndOfTick(() -> player.getStatSystem().updateSlot(slot));
            return;
        }

        player.getAbilitySystem().tryUse(itemStack, ExecutionType.RIGHT_CLICK);
    }

    private static void mapArmorToSlot(ItemSlot slot, Material... materials) {
        for (Material material : materials) {
            ARMOR_SLOT_MAP.put(material, slot);
        }
    }
}