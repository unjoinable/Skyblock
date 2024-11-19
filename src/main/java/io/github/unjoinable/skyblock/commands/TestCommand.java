package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class TestCommand extends Command {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);
        });
    }
}
