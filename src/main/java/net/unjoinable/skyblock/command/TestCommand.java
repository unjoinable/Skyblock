package net.unjoinable.skyblock.command;

import net.unjoinable.skyblock.entity.mobs.Zombie;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;

public class TestCommand extends SkyblockCommand {

    public TestCommand() {
        super("test");
        addSyntax((sender, ctx) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);
            new Zombie().spawn(2, player.getInstance(), player.getPosition().add(2));
        });
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
