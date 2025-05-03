package net.skyblock.event.listeners.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.skyblock.item.inventory.ItemSlot;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for handling when a player changes their held hotbar slot.
 * <p>
 * Updates the player's stats based on their main hand item.
 */
public class PlayerChangeHeldSlotListener implements EventListener<PlayerChangeHeldSlotEvent> {

    /**
     * Returns the type of event this listener handles.
     *
     * @return {@link PlayerChangeHeldSlotEvent} class
     */
    @Override
    public @NotNull Class<PlayerChangeHeldSlotEvent> eventType() {
        return PlayerChangeHeldSlotEvent.class;
    }

    /**
     * Called when a player changes their held slot.
     * <p>
     * Updates stats related to the item in the player's main hand.
     *
     * @param event the event triggered when the player switches slots
     * @return {@link Result#SUCCESS} to continue event processing
     */
    @Override
    public @NotNull Result run(@NotNull PlayerChangeHeldSlotEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

        MinecraftServer.getSchedulerManager().scheduleEndOfTick(() -> player.getStatsManager().update(ItemSlot.MAIN_HAND));

        return Result.SUCCESS;
    }
}
