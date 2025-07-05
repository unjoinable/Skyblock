package net.unjoinable.skyblock.event.listener.player.combat;

import net.unjoinable.skyblock.event.custom.PlayerLeftClickEvent;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.player.SkyblockPlayer;

import java.util.function.Consumer;

/**
 * Handles custom player left-click events.
 */
public class PlayerLeftClickListener implements Consumer<PlayerLeftClickEvent> {

    @Override
    public void accept(PlayerLeftClickEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        player.getAbilitySystem().tryUse(event.getItemStack(), ExecutionType.LEFT_CLICK);
    }
}