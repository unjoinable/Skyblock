package com.github.unjoinable.skyblock.commands;

import com.github.unjoinable.skyblock.gui.inventories.SkyblockMenu;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class MenuCommand extends Command {

    public MenuCommand() {
        super("menu");

        addSyntax((sender, _) -> {
            SkyblockPlayer player = (SkyblockPlayer) sender;
            SkyblockMenu menu = new SkyblockMenu(player);
            player.openInventory(menu);
        });
    }
}
