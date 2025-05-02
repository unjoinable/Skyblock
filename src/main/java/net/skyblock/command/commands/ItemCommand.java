package net.skyblock.command.commands;

import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.skyblock.Skyblock;
import net.skyblock.command.SkyblockCommand;
import net.skyblock.item.SkyblockItem;
import net.skyblock.player.SkyblockPlayer;
import net.skyblock.player.rank.PlayerRank;
import net.skyblock.registry.ItemRegistry;

/**
 * Command for giving items to players.
 * <p>
 * This command allows players to give items to themselves or others based on the item ID.
 * The command supports two syntaxes:
 * <ul>
 *   <li>/item <itemId> - Gives the item to the command sender</li>
 *   <li>/item <itemId> <target> - Gives the item to a specified target player</li>
 * </ul>
 */
public class ItemCommand extends SkyblockCommand {

    /**
     * Constructs the "item" command.
     * <p>
     * This sets up two syntaxes for the command:
     * 1. /item <itemId> - gives the item to the player issuing the command.
     * 2. /item <itemId> <target> - gives the item to a specified target player.
     */
    public ItemCommand() {
        super("item");
        Argument<String> itemArg = ArgumentType.String("itemId")
                .setSuggestionCallback((_, _, suggestion) -> Skyblock.getInstance().getItemRegistry().iterator()
                        .forEachRemaining(item -> suggestion.addEntry(new SuggestionEntry(item.itemId()))));
        ArgumentEntity playerArg = ArgumentType.Entity("target").onlyPlayers(true).singleEntity(true);

        // Basic syntax: /item <itemId>
        addSyntax((sender, context) -> {
            String itemId = context.get(itemArg);

            // Only players can use this command
            if (!(sender instanceof SkyblockPlayer player)) {
                return;
            }

            giveItem(player, itemId);
        }, itemArg);

        // Extended syntax: /item <itemId> <target>
        addSyntax((sender, context) -> {
            String itemId = context.get(itemArg);
            Player target = context.get(playerArg).findFirstPlayer(sender);

            if (target != null) {
                giveItem((SkyblockPlayer) target, itemId);
            }
        }, itemArg, playerArg);
    }

    /**
     * Returns the rank required to execute this command.
     *
     * @return the required {@link PlayerRank}, which is {@link PlayerRank#DEFAULT}
     */
    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }

    /**
     * Gives the specified item to the player.
     * <p>
     * This method fetches the item from the {@link ItemRegistry} by its ID and adds it to the player's inventory.
     * If the item is found, it is converted to an item stack and added to the player's inventory.
     *
     * @param player the player to whom the item will be given
     * @param itemId the ID of the item to be given
     */
    private void giveItem(SkyblockPlayer player, String itemId) {
        ItemRegistry itemRegistry = Skyblock.getInstance().getItemRegistry();
        SkyblockItem item = itemRegistry.get(itemId);

        if (item != null) {
            player.getInventory().addItemStack(Skyblock.getInstance().getProcessor().toItemStack(item));
        }
    }
}
