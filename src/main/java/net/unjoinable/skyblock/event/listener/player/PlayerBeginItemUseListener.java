package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.item.Material;

import java.util.function.Consumer;

public class PlayerBeginItemUseListener implements Consumer<PlayerBeginItemUseEvent> {

    @Override
    public void accept(PlayerBeginItemUseEvent event) {
        if (event.getItemStack().material() == Material.BOW) {
            event.setCancelled(true);
        }
    }
}
