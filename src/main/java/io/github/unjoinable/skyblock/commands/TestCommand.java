package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.entity.ThrowableStack;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.item.Material;

public class TestCommand extends Command {

    public TestCommand() {
        super("test");

        addSyntax((sender, _) -> {
            SkyblockPlayer player = (SkyblockPlayer) sender;
            ThrowableStack stack = new ThrowableStack(player, Material.DIAMOND_AXE ,50);
            stack.setInstance(player.getInstance(),player.getPosition());
            stack.push(player);

        });
    }
}
