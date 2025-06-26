package net.unjoinable.skyblock.command;

import net.unjoinable.skyblock.player.rank.PlayerRank;
import net.unjoinable.skyblock.registry.registries.ItemRegistry;

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
    public ItemCommand(ItemRegistry registry) {
        super("item");
    }

    /**
     * @return Hypixel Staff rank required to use this command
     */
    @Override
    public PlayerRank getRequiredRank() {
        return PlayerRank.HYPIXEL_STAFF;
    }
}