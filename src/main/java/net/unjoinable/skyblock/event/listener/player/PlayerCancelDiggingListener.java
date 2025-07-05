package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.player.PlayerCancelDiggingEvent;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.listener.player.PlayerListenerConstants.IS_DIGGING;

/**
 * Handles player cancel digging events.
 */
public class PlayerCancelDiggingListener implements Consumer<PlayerCancelDiggingEvent> {

    @Override
    public void accept(PlayerCancelDiggingEvent event) {
        event.getPlayer().removeTag(IS_DIGGING);
    }
}