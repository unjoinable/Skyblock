package io.github.unjoinable.skyblock.user;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ActionSlot {
    private final SkyblockPlayer player;
    private ItemStack defaultItem; //if no replacements are there it will display this.

    public ActionSlot(@NotNull SkyblockPlayer player, @NotNull ItemStack defaultItem) {
        this.player = player;
        this.defaultItem = defaultItem;

        //set slot
        player.getInventory().setItemStack(8, defaultItem);
    }

    public void addReplacement(@NotNull ItemStack replacementItem) {
        player.getInventory().setItemStack(8, replacementItem);
    }

    public void removeReplacement() {
        player.getInventory().setItemStack(8, defaultItem);
    }
}
