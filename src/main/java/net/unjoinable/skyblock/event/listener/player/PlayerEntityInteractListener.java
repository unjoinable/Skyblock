package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.player.PlayerEntityInteractEvent;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.listener.player.PlayerListenerConstants.IGNORE_ANIMATION;

/**
 * Handles player entity interaction events.
 */
public class PlayerEntityInteractListener implements Consumer<PlayerEntityInteractEvent> {

    @Override
    public void accept(PlayerEntityInteractEvent event) {
        event.getPlayer().setTag(IGNORE_ANIMATION, true);
    }
}