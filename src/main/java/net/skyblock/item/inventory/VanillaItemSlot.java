package net.skyblock.item.inventory;

import net.minestom.server.entity.EquipmentSlot;
import net.skyblock.item.definition.SkyblockItem;
import net.skyblock.item.io.ItemProcessor;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Standard equipment slots based on vanilla Minecraft
 */
public enum VanillaItemSlot implements ItemSlot {
    HELMET("helmet", (player, processor) ->
            processor.toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.HELMET.armorSlot()))),

    CHESTPLATE("chestplate", (player, processor) ->
            processor.toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.CHESTPLATE.armorSlot()))),

    LEGGINGS("leggings", (player, processor) ->
            processor.toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.LEGGINGS.armorSlot()))),

    BOOTS("boots", (player, processor) ->
            processor.toSkyblockItem(player.getInventory().getItemStack(EquipmentSlot.BOOTS.armorSlot()))),

    MAIN_HAND("main_hand", (player, processor) ->
            processor.toSkyblockItem(player.getItemInMainHand())),

    OFF_HAND("off_hand", (player, processor) ->
            processor.toSkyblockItem(player.getItemInOffHand()));

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
    public @Nullable SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor) {
        return itemGetter.getItem(player, processor);
    }

    /**
     * Functional interface for retrieving items from slots
     */
    @FunctionalInterface
    private interface SlotItemGetter {
        @Nullable SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor);
    }
}
