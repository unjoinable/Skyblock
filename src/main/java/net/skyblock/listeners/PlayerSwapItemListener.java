package net.skyblock.listeners;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.skyblock.player.ItemSlot;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerSwapItemListener implements EventListener<PlayerSwapItemEvent> {

    @Override
    public @NotNull Class<PlayerSwapItemEvent> eventType() {
        return PlayerSwapItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSwapItemEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        System.out.printf("test");
        player.getStatsManager().update(ItemSlot.MAIN_HAND);
        return Result.SUCCESS;
    }
}
