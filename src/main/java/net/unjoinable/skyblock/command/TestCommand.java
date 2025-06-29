package net.unjoinable.skyblock.command;

import net.unjoinable.skyblock.entity.mobs.Zombie;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;

public class TestCommand extends SkyblockCommand {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
            new Zombie().spawn(1, ((SkyblockPlayer) sender).getInstance(), ((SkyblockPlayer) sender).getPosition().add(0, 1, 0));
        });
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
