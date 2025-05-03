package net.skyblock.event.listeners.inventory;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryCloseListener implements EventListener<InventoryCloseEvent> {

    @Override
    public @NotNull Class<InventoryCloseEvent> eventType() {
        return InventoryCloseEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InventoryCloseEvent event) {
        //SkyblockInventory.handleInventoryClose(event);
        return Result.SUCCESS;
    }
}
