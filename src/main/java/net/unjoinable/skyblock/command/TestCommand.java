package net.unjoinable.skyblock.command;

import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;

import java.util.Random;

public class TestCommand extends SkyblockCommand {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);
            player.getEconomySystem().setBits(new Random().nextLong());
        });
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
