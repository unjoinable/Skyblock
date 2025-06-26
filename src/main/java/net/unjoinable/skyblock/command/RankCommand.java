package net.unjoinable.skyblock.command;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;

public class RankCommand extends SkyblockCommand {

    public RankCommand() {
        super("rank");
        ArgumentEnum<PlayerRank> rankArg =  new ArgumentEnum<>("rank", PlayerRank.class);

        addSyntax((sender, args) -> {
            SkyblockPlayer player = (SkyblockPlayer) sender;
            PlayerRank rank = args.get(rankArg);
            player.setPlayerRank(rank);
        }, rankArg);
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
