package net.unjoinable.skyblock.event.listener.player.combat;

import net.minestom.server.event.player.PlayerCancelDiggingEvent;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent.IS_DIGGING;

/**
 * Handles player cancel digging events.
 */
public class PlayerCancelDiggingListener implements Consumer<PlayerCancelDiggingEvent> {

    @Override
    public void accept(PlayerCancelDiggingEvent event) {
        event.getPlayer().removeTag(IS_DIGGING);
    }
}