package net.unjoinable.skyblock.event.listener.player.interaction;

import net.minestom.server.event.player.PlayerBlockInteractEvent;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent.IGNORE_ANIMATION;

/**
 * Handles player block interaction events.
 */
public class PlayerBlockInteractListener implements Consumer<PlayerBlockInteractEvent> {

    @Override
    public void accept(PlayerBlockInteractEvent event) {
        event.getPlayer().setTag(IGNORE_ANIMATION, true);
    }
}