package net.unjoinable.player.ui.inventory;

import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.service.ItemProcessor;
import net.unjoinable.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

public class ActionSlot implements ItemSlot {

    @Override
    public String getName() {
        return "action_slot";
    }

    @Override
    public @NotNull SkyblockItem getItem(@NotNull SkyblockPlayer player, @NotNull ItemProcessor processor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
