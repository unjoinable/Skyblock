package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.item.ItemDropEvent;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent.IGNORE_ANIMATION;

/**
 * Handles item drop events.
 */
public class ItemDropListener implements Consumer<ItemDropEvent> {

    @Override
    public void accept(ItemDropEvent event) {
        event.getPlayer().setTag(IGNORE_ANIMATION, true);
    }
}