package net.unjoinable.skyblock.event.listener.player.combat;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent.IGNORE_ANIMATION;
import static net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent.IS_DIGGING;

/**
 * Handles player hand animation events and left-click detection.
 */
public class PlayerHandAnimationListener implements Consumer<PlayerHandAnimationEvent> {

    @Override
    public void accept(PlayerHandAnimationEvent event) {
        SkyblockPlayer player = ((SkyblockPlayer) event.getPlayer());

        if (player.hasTag(IGNORE_ANIMATION)) {
            player.removeTag(IGNORE_ANIMATION);
            return;
        }

        if (player.hasTag(IS_DIGGING)) return;

        if (event.getHand() == PlayerHand.MAIN) {
            MinecraftServer.getGlobalEventHandler().call(new PlayerLeftClickEvent(player, player.getItemInMainHand()));
        }
    }
}