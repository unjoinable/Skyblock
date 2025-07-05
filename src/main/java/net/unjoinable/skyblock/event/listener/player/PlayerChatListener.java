package net.unjoinable.skyblock.event.listener.player;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerChatEvent;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;

import java.util.function.Consumer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

/**
 * Handles player chat events and message formatting.
 */
public class PlayerChatListener implements Consumer<PlayerChatEvent> {

    @Override
    public void accept(PlayerChatEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        PlayerRank rank = player.getPlayerRank();

        Component message = rank == PlayerRank.DEFAULT
                ? text(player.getUsername() + ": " + event.getRawMessage(), GRAY)
                : rank.getComponentPrefix()
                .append(text(" " + player.getUsername(), rank.getColor()))
                .append(text(": " + event.getRawMessage(), WHITE));

        event.setFormattedMessage(message);
    }
}