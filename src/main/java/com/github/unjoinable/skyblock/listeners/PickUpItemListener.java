package com.github.unjoinable.skyblock.listeners;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PickUpItemListener implements EventListener<PickupItemEvent> {

    @Override
    public @NotNull Class<PickupItemEvent> eventType() {
        return PickupItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PickupItemEvent event) {
        ItemStack item = event.getItemStack();
        if (event.getEntity() instanceof final Player player) {
            if (item.getTag(ItemDropListener.DROPPED_BY_PLAYER) == player) {
                player.getInventory().addItemStack(item);
            }
        }
        return Result.SUCCESS;
    }
}
