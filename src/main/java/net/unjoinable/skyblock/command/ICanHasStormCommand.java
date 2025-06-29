package net.unjoinable.skyblock.command;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.player.rank.PlayerRank;
import net.unjoinable.skyblock.utils.MiniString;

public class ICanHasStormCommand extends SkyblockCommand {
    private static final Component MESSAGE = MiniString.asComponent
            ("<green>This server runs Hystorm, heavily modified version of Minestom by the Hystorm Dev Team. [1970.1.1]");

    public ICanHasStormCommand() {
        super("icanhasstorm");
        addSyntax((sender, _) -> sender.sendMessage(MESSAGE));
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
