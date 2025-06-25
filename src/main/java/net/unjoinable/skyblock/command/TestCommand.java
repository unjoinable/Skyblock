package net.unjoinable.skyblock.command;

import net.minestom.server.command.builder.Command;
import net.unjoinable.skyblock.player.SkyblockPlayer;

import java.util.Random;

public class TestCommand extends Command {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);
            player.getEconomySystem().setBits(new Random().nextLong());
        });
    }
}
