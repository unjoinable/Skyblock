package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.inventory.click.ClickType;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent.IGNORE_ANIMATION;

/**
 * Handles inventory click events.
 */
public class InventoryClickListener implements Consumer<InventoryClickEvent> {

    @Override
    public void accept(InventoryClickEvent event) {
        if (event.getClickType() == ClickType.DROP) {
            event.getPlayer().setTag(IGNORE_ANIMATION, true);
        }
    }
}