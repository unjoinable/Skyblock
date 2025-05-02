package net.skyblock.command.commands;

import net.minestom.server.entity.Player;
import net.skyblock.Skyblock;
import net.skyblock.command.SkyblockCommand;
import net.skyblock.item.Reforge;
import net.skyblock.item.SkyblockItem;
import net.skyblock.item.component.impl.HotPotatoBookComponent;
import net.skyblock.item.component.impl.ReforgeComponent;
import net.skyblock.item.enums.Rarity;
import net.skyblock.player.rank.PlayerRank;
import net.skyblock.stats.StatProfile;

import java.util.Map;

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
            SkyblockItem item = Skyblock.getInstance().getItemRegistry().get("HYPERION");
            StatProfile profile = new StatProfile(true);
            Reforge reforge = new Reforge("uwu", Map.of(Rarity.LEGENDARY, profile));


            assert item != null;
            var w = new SkyblockItem(item.itemId(), item.components().with(new ReforgeComponent(reforge)).with(new HotPotatoBookComponent(10)));
            ((Player) sender).getInventory().addItemStack(Skyblock.getInstance().getProcessor().toItemStack(w));
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
