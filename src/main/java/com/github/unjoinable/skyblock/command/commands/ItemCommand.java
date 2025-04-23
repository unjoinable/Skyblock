package com.github.unjoinable.skyblock.command.commands;

import com.github.unjoinable.skyblock.Skyblock;
import com.github.unjoinable.skyblock.command.SkyblockCommand;
import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.player.rank.PlayerRank;
import com.github.unjoinable.skyblock.player.SkyblockPlayer;
import com.github.unjoinable.skyblock.registry.ItemRegistry;
import com.github.unjoinable.skyblock.registry.Registry;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;

public class ItemCommand extends SkyblockCommand {

    public ItemCommand() {
        super("item");
        Argument<String> itemArg = ArgumentType.String("itemId")
                .setSuggestionCallback((_, _, suggestion) -> Registry.ITEM_REGISTRY.iterator()
                        .forEachRemaining(item -> suggestion.addEntry(new SuggestionEntry(item.itemId()))));
        ArgumentEntity playerArg = ArgumentType.Entity("target").onlyPlayers(true).singleEntity(true);

        // Basic command: /item <itemId>
        addSyntax((sender, context) -> {
            String itemId = context.get(itemArg);

            // Only players can use this
            if (!(sender instanceof SkyblockPlayer player)) {
                return;
            }

            giveItem(player, itemId);
        }, itemArg);

        // Extended command: /item <itemId> <target>
        addSyntax((sender, context) -> {
            String itemId = context.get(itemArg);
            Player target = context.get(playerArg).findFirstPlayer(sender);

            if (target != null) {
                giveItem((SkyblockPlayer) target, itemId);
            }
        }, itemArg, playerArg);
    }

    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.DEFAULT;
    }

    private void giveItem(SkyblockPlayer player, String itemId) {
        ItemRegistry itemRegistry = Registry.ITEM_REGISTRY;
        SkyblockItem item = itemRegistry.get(itemId);

        if (item != null) {
            player.getInventory().addItemStack(Skyblock.getProcessor().toItemStack(item));
        }
    }
}