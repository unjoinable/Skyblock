package net.unjoinable.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;
import net.unjoinable.player.SkyblockPlayer;

public class TestCommand extends Command {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);
            player.setGameMode(GameMode.SPECTATOR);
        });
    }
}
