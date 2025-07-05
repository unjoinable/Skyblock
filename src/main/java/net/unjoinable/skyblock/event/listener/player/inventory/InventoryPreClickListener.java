package net.unjoinable.skyblock.event.listener.player.inventory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.ui.inventory.ItemSlot;
import net.unjoinable.skyblock.player.ui.inventory.VanillaItemSlot;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Handles inventory pre-click events and equipment stat updates.
 */
public class InventoryPreClickListener implements Consumer<InventoryPreClickEvent> {

    @Override
    public void accept(InventoryPreClickEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

        int clickedSlot = event.getSlot();
        ItemSlot itemSlot = getItemSlot(clickedSlot, player);

        if (itemSlot != null) {
            MinecraftServer.getSchedulerManager()
                    .scheduleEndOfTick(() -> player.getStatSystem().updateSlot(itemSlot));
        }
    }

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
}