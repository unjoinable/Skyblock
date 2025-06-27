package net.unjoinable.skyblock.command;

import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;
import net.unjoinable.skyblock.registry.registries.ItemRegistry;
import net.unjoinable.skyblock.utils.NamespaceId;

/**
 * Admin command for spawning items from the item registry.
 * Requires Hypixel Staff Rank
 */
public class ItemCommand extends SkyblockCommand {

    /**
     * Creates the item command with access to the item registry.
     *
     * @param registry The item registry containing all default items
     */
    public ItemCommand(ItemRegistry registry, ItemProcessor itemProcessor) {
        super("item");
        Argument<String> itemArg = ArgumentType.String("item")
                .setSuggestionCallback((_, _, suggestion) ->
                        registry.values().forEach(item ->
                                suggestion.addEntry(new SuggestionEntry(item.metadata().id().toString()))
                        )
                );

        addSyntax((sender, context) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);
            String idAsStr = context.get(itemArg);
            NamespaceId id = NamespaceId.fromString(idAsStr);
            registry.get(id).ifPresent(item -> player.getInventory().addItemStack(itemProcessor.toItemStack(item)));
        }, itemArg);
    }

    /**
     * @return Hypixel Staff rank required to use this command
     */
    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.HYPIXEL_STAFF;
    }
}