package net.unjoinable.skyblock.event.listener.player.combat;

import net.minestom.server.event.player.PlayerFinishDiggingEvent;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent.IS_DIGGING;

/**
 * Handles player finish digging events.
 */
public class PlayerFinishDiggingListener implements Consumer<PlayerFinishDiggingEvent> {

    @Override
    public void accept(PlayerFinishDiggingEvent event) {
        event.getPlayer().removeTag(IS_DIGGING);
    }
}