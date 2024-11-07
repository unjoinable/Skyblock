package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.registry.registries.ItemRegistry;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import io.github.unjoinable.skyblock.util.StringUtils;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemCommand extends Command {

    public ItemCommand(@NotNull String name) {
        super(name);
        Argument<String> idArg = ArgumentType.String("item_id").setSuggestionCallback((_, _, suggestion) -> {
            ItemRegistry.getInstance().forEach(item -> {
                suggestion.addEntry(new SuggestionEntry(item.id().toString()));
            });
        });

        addSyntax((sender, context) -> {

            try {
                NamespacedId itemId = NamespacedId.fromString(context.get(idArg));
                var item = ItemRegistry.getInstance().getItem(itemId);
                ((Player) sender).getInventory().addItemStack(item.toItemStack((SkyblockPlayer) sender));
                sender.sendMessage(StringUtils.toComponent("<green>{} added to your inventory", itemId.key().toUpperCase()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, idArg);
    }
}
