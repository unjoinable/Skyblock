package net.skyblock.command.impl;

import net.skyblock.command.base.SkyblockCommand;
import net.skyblock.player.rank.PlayerRank;

/**
 * A basic test command used for verifying the command framework.
 * <p>
 * This command requires the {@link PlayerRank#DEFAULT} rank and performs no action when executed.
 * It's typically used for debugging or as a template for new commands.
 */
public class TestCommand extends SkyblockCommand {

    /**
     * Constructs the test command with the name "test".
     * Defines its syntax and handler.
     */
    public TestCommand() {
        super("test");
    }

    /**
     * Specifies the rank required to execute this command.
     *
     * @return the minimum rank required to use the command, which is {@link PlayerRank#DEFAULT}
     */
    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }
}
