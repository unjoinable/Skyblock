package net.skyblock.command.commands;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.skyblock.command.SkyblockCommand;
import net.skyblock.player.SkyblockPlayer;
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

        // Test Syntax
        addSyntax((sender, _) -> {
            SkyblockPlayer player = (SkyblockPlayer) sender;
            Tag<Boolean> tag = Tag.Boolean("test").defaultValue(false);
            ItemStack itemStack = ItemStack.of(Material.STONE);
            System.out.println(itemStack.hasTag(tag));
        });

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
