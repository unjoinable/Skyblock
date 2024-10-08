package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.Skyblock;
import io.github.unjoinable.skyblock.item.SkyblockItemProcessor;
import io.github.unjoinable.skyblock.registry.registries.ItemRegistry;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static io.github.unjoinable.skyblock.util.MiniMessageTemplate.MM;

public class ItemCommand extends Command {

    public ItemCommand(@NotNull String name) {
        super(name);
        Argument<String> idArg = ArgumentType.String("item_id").setSuggestionCallback((_, _, suggestion) -> {
            ItemRegistry.getInstance().forEach(item -> {
                suggestion.addEntry(new SuggestionEntry(item.id().toLowerCase()));
            });
        });

        addSyntax((sender, context) -> {

            try {
                String itemId = context.get(idArg);
                SkyblockItemProcessor processor = Skyblock.getItemProcessor();
                var item = processor.getItem(itemId);
                ((Player) sender).getInventory().addItemStack(item.toItemStack());
                sender.sendMessage(MM."<green>\{itemId.toUpperCase()} added to your inventory");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, idArg);
    }
}
