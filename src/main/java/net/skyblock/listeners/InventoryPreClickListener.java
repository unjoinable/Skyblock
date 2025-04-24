package net.skyblock.listeners;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.skyblock.player.ItemSlot;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public class InventoryPreClickListener implements EventListener<InventoryPreClickEvent> {

    @Override
    public @NotNull Class<InventoryPreClickEvent> eventType() {
        return InventoryPreClickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InventoryPreClickEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        int clickedSlot = event.getSlot();
        ItemSlot itemSlot = null;

        switch (clickedSlot) {
            case 41 -> itemSlot = ItemSlot.HELMET;
            case 42 -> itemSlot = ItemSlot.CHESTPLATE;
            case 43 -> itemSlot = ItemSlot.LEGGINGS;
            case 44 -> itemSlot = ItemSlot.BOOTS;
            default -> {
                if (clickedSlot == player.getHeldSlot())  itemSlot = ItemSlot.MAIN_HAND;
            }
        }

        if (itemSlot != null) {
            player.getStatsManager().update(itemSlot);
        }

        return Result.SUCCESS;
    }
}