package net.skyblock.item.inventory;

import net.skyblock.item.io.ItemProcessor;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating PlayerItemProvider instances
 */
public class ItemProviderFactory {
    private final ItemProcessor processor;

    /**
     * Creates a new ItemProviderFactory
     * @param processor The item processor to use
     */
    public ItemProviderFactory(@NotNull ItemProcessor processor) {
        this.processor = processor;
    }

    /**
     * Creates a PlayerItemProvider for a player
     * @param player The player
     * @return A new PlayerItemProvider
     */
    public PlayerItemProvider createProvider(@NotNull SkyblockPlayer player) {
        return new PlayerItemProvider(player, processor);
    }
}