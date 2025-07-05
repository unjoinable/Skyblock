package net.unjoinable.skyblock.event.listener.player.inventory;

import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.ui.inventory.VanillaItemSlot;

import java.util.function.Consumer;

/**
 * Handles player item swap events and stat updates.
 */
public class PlayerSwapItemListener implements Consumer<PlayerSwapItemEvent> {

    @Override
    public void accept(PlayerSwapItemEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        player.getStatSystem().updateSlot(VanillaItemSlot.MAIN_HAND);
    }
}