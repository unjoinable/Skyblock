package net.unjoinable.skyblock.event.listener.player.inventory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.ui.inventory.VanillaItemSlot;

import java.util.function.Consumer;

/**
 * Handles player held slot change events and stat updates.
 */
public class PlayerChangeHeldSlotListener implements Consumer<PlayerChangeHeldSlotEvent> {

    @Override
    public void accept(PlayerChangeHeldSlotEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

        MinecraftServer.getSchedulerManager()
                .scheduleEndOfTick(() -> player.getStatSystem().updateSlot(VanillaItemSlot.MAIN_HAND));
    }
}