package com.github.unjoinable.skyblock.command.commands;

import com.github.unjoinable.skyblock.Skyblock;
import com.github.unjoinable.skyblock.command.SkyblockCommand;
import com.github.unjoinable.skyblock.player.rank.PlayerRank;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class TestCommand extends SkyblockCommand {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
            ItemStack item = ((Player) sender).getItemInMainHand();
            System.out.println(Skyblock.getProcessor().toSkyblockItem(item).itemId());
            Skyblock.getProcessor().toSkyblockItem(item).components().asMap().values().forEach(System.out::println);
        });
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
