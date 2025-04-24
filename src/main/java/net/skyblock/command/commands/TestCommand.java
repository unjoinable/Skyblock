package net.skyblock.command.commands;

import net.skyblock.command.SkyblockCommand;
import net.skyblock.player.rank.PlayerRank;

public class TestCommand extends SkyblockCommand {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
        });
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
