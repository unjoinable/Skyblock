package net.unjoinable.skyblock.player.ui.inventory;

import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.player.SkyblockPlayer;

public class ActionSlot implements ItemSlot {

    @Override
    public String getName() {
        return "action_slot";
    }

    @Override
    public SkyblockItem getItem(SkyblockPlayer player, ItemProcessor processor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
