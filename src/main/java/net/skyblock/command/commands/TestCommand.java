package net.skyblock.command.commands;

import net.skyblock.Skyblock;
import net.skyblock.command.SkyblockCommand;
import net.skyblock.player.rank.PlayerRank;
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
