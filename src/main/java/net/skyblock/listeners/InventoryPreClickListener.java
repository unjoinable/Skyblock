package net.skyblock.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.skyblock.player.ItemSlot;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for {@link InventoryPreClickEvent}, which is triggered before a player interacts with an inventory slot.
 * <p>
 * This listener checks if the clicked slot corresponds to an armor or main-hand slot and updates
 * the player's stats accordingly.
 */
public class InventoryPreClickListener implements EventListener<InventoryPreClickEvent> {

    /**
     * Returns the class of the event this listener handles.
     *
     * @return the {@link InventoryPreClickEvent} class
     */
    @Override
    public @NotNull Class<InventoryPreClickEvent> eventType() {
        return InventoryPreClickEvent.class;
    }

    /**
     * Called before a player clicks on an inventory slot.
     * <p>
     * If the clicked slot corresponds to a known equipment slot (helmet, chestplate, etc.)
     * or the currently held item, the appropriate stats are updated.
     *
     * @param event the inventory pre-click event
     * @return {@link Result#SUCCESS} to continue event processing
     */
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
                if (clickedSlot == player.getHeldSlot()) {
                    itemSlot = ItemSlot.MAIN_HAND;
                }
            }
        }

        if (itemSlot != null) {
            ItemSlot finalItemSlot = itemSlot;
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                player.getStatsManager().update(finalItemSlot);
            });
        }

        return Result.SUCCESS;
    }
}
