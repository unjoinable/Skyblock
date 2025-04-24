package net.skyblock.listeners;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.skyblock.player.ItemSlot;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerChangeHeldSlotListener implements EventListener<PlayerChangeHeldSlotEvent> {

    @Override
    public @NotNull Class<PlayerChangeHeldSlotEvent> eventType() {
        return PlayerChangeHeldSlotEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerChangeHeldSlotEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        player.getStatsManager().update(ItemSlot.MAIN_HAND);
        return Result.SUCCESS;
    }
}
