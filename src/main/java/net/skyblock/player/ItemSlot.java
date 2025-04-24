package net.skyblock.player;

import net.skyblock.Skyblock;
import net.skyblock.item.SkyblockItem;
import net.minestom.server.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Unified equipment slot system that covers both vanilla and custom slots.
 * Each enum constant contains a function to retrieve the corresponding item for a player.
 */
public enum ItemSlot {
    HELMET(player -> Skyblock.getInstance().getProcessor().toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.HELMET.armorSlot()))),
    CHESTPLATE(player -> Skyblock.getInstance().getProcessor().toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.CHESTPLATE.armorSlot()))),
    LEGGINGS(player -> Skyblock.getInstance().getProcessor().toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.LEGGINGS.armorSlot()))),
    BOOTS(player -> Skyblock.getInstance().getProcessor().toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.BOOTS.armorSlot()))),

    MAIN_HAND(player -> Skyblock.getInstance().getProcessor().toSkyblockItem(player.getItemInMainHand())),
    ;


    // Function to retrieve the SkyblockItem for this slot from a player
    private final Function<SkyblockPlayer, SkyblockItem> itemGetter;

    ItemSlot(Function<SkyblockPlayer, SkyblockItem> itemGetter) {
        this.itemGetter = itemGetter;
    }

    /**
     * Gets the SkyblockItem equipped in this slot for the given player
     *
     * @param player The player to get the item from
     * @return The SkyblockItem in this slot, or null if none
     */
    public @Nullable SkyblockItem getItem(SkyblockPlayer player) {
        return itemGetter.apply(player);
    }
}