package net.unjoinable.skyblock.command;

import net.unjoinable.skyblock.player.rank.PlayerRank;

public class TestCommand extends SkyblockCommand {

    public TestCommand() {
        super("test");
        addSyntax((sender, ctx) -> {

        });
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
