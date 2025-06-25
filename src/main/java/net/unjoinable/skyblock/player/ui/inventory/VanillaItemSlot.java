package net.unjoinable.skyblock.player.ui.inventory;

import net.minestom.server.entity.EquipmentSlot;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.player.SkyblockPlayer;

/**
 * Standard equipment slots based on vanilla Minecraft
 */
public enum VanillaItemSlot implements ItemSlot {
    HELMET("helmet", (player, processor) ->
            processor.fromItemStack(player.getInventory().getItemStack(EquipmentSlot.HELMET.armorSlot()))),

    CHESTPLATE("chestplate", (player, processor) ->
            processor.fromItemStack(player.getInventory().getItemStack(EquipmentSlot.CHESTPLATE.armorSlot()))),

    LEGGINGS("leggings", (player, processor) ->
            processor.fromItemStack(player.getInventory().getItemStack(EquipmentSlot.LEGGINGS.armorSlot()))),

    BOOTS("boots", (player, processor) ->
            processor.fromItemStack(player.getInventory().getItemStack(EquipmentSlot.BOOTS.armorSlot()))),

    MAIN_HAND("main_hand", (player, processor) ->
            processor.fromItemStack(player.getItemInMainHand())),

    OFF_HAND("off_hand", (player, processor) ->
            processor.fromItemStack(player.getItemInOffHand()));

    private final String name;
    private final SlotItemGetter itemGetter;

    VanillaItemSlot(String name, SlotItemGetter itemGetter) {
        this.name = name;
        this.itemGetter = itemGetter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor) {
        return itemGetter.getItem(player, processor);
    }

    /**
     * Functional interface for retrieving items from slots
     */
    @FunctionalInterface
    private interface SlotItemGetter {
        SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor);
    }
}
