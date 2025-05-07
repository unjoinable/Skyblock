package net.skyblock.item.io;

import net.minestom.server.item.ItemStack;
import net.skyblock.item.definition.SkyblockItem;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for processing and converting between Skyblock items and Minestom ItemStacks
 */
public interface ItemProcessor {

    /**
     * Converts a SkyblockItem to an ItemStack
     *
     * @param skyblockItem The Skyblock item to convert
     * @return The resulting ItemStack
     */
    @NotNull ItemStack toItemStack(@NotNull SkyblockItem skyblockItem);

    /**
     * Converts an ItemStack to a SkyblockItem
     *
     * @param itemStack The ItemStack to convert
     * @return The resulting SkyblockItem
     */
    @NotNull SkyblockItem toSkyblockItem(@NotNull ItemStack itemStack);
}