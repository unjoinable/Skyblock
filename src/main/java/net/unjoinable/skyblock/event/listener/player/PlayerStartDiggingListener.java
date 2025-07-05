package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.player.PlayerStartDiggingEvent;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.listener.player.PlayerListenerConstants.IS_DIGGING;

/**
 * Handles player start digging events.
 */
public class PlayerStartDiggingListener implements Consumer<PlayerStartDiggingEvent> {

    @Override
    public void accept(PlayerStartDiggingEvent event) {
        event.getPlayer().setTag(IS_DIGGING, true);
    }
}