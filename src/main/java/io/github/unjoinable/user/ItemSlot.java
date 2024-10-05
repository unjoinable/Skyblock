package io.github.unjoinable.user;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

/**
 * @since 1.0.0
 * @author Swofty
 */
public enum ItemSlot {
    MAIN_HAND((objectEntry) -> objectEntry.getInventory().getItemInMainHand()),
    HELMET((objectEntry) -> objectEntry.getInventory().getHelmet()),
    CHESTPLATE((objectEntry) -> objectEntry.getInventory().getChestplate()),
    LEGGINGS((objectEntry) -> objectEntry.getInventory().getLeggings()),
    BOOTS((objectEntry) -> objectEntry.getInventory().getBoots()),
    ;

    private final Function<Player, ItemStack> retriever;
    private static final Collection<ItemSlot> VALUES = Arrays.asList(values());

    /**
     * @since 1.0.0
     */
    ItemSlot(Function<Player, ItemStack> retriever) {
        this.retriever = retriever;
    }

    /**
     * @param player The player from whom the item is obtained.
     * @return The item in the specific slot of player
     * @since 1.0.0
     */
    public ItemStack get(Player player) {
        return this.retriever.apply(player);
    }

    public static Collection<ItemSlot> getValues() {
        return VALUES;
    }
}
