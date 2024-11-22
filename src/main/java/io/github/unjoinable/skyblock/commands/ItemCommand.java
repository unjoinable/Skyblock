package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.registry.registries.ItemRegistry;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import io.github.unjoinable.skyblock.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;

public class ItemCommand extends Command {
    private static final Component NO_ITEM_FOUND = StringUtils.toComponent("<red>No item found with that name");

    public ItemCommand() {
        super("item");
        //arguments
        Argument<String> idArg = ArgumentType.String("item_id").setSuggestionCallback((_, _, suggestion) -> {
            ItemRegistry.getInstance().forEach(item -> {
                suggestion.addEntry(new SuggestionEntry(item.id().toString()));
            });
        });
        //syntax
        addSyntax((sender, context) -> {
            try {
                NamespacedId itemId = NamespacedId.fromString(context.get(idArg));
                SkyblockItem item = ItemRegistry.getInstance().get(itemId);
                ((Player) sender).getInventory().addItemStack(item.toItemStack((SkyblockPlayer) sender));
                sender.sendMessage(StringUtils.toComponent("<green>{} added to your inventory", itemId.key().toUpperCase()));
            } catch (Exception _) { //ignoring exception
                sender.sendMessage(NO_ITEM_FOUND);
            }
        }, idArg);
    }
}
