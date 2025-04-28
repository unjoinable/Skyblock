package net.skyblock.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.skyblock.player.ItemSlot;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the {@link PlayerSwapItemEvent}, which occurs when a player swaps items
 * between their main hand and off-hand.
 * <p>
 * This listener updates the player's stats for the main hand after the swap.
 */
public class PlayerSwapItemListener implements EventListener<PlayerSwapItemEvent> {

    /**
     * Returns the class of the event this listener is interested in.
     *
     * @return the {@link PlayerSwapItemEvent} class
     */
    @Override
    public @NotNull Class<PlayerSwapItemEvent> eventType() {
        return PlayerSwapItemEvent.class;
    }

    /**
     * Called when a player performs an item swap (main-hand ↔ off-hand).
     * Updates the player's statistics for the main hand item slot.
     *
     * @param event the player swap item event
     * @return {@link Result#SUCCESS} to allow further event processing
     */
    @Override
    public @NotNull Result run(@NotNull PlayerSwapItemEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

        MinecraftServer.getSchedulerManager().scheduleEndOfTick(() -> player.getStatsManager().update(ItemSlot.MAIN_HAND));

        return Result.SUCCESS;
    }
}
