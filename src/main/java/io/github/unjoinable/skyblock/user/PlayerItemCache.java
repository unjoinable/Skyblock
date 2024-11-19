package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache system to store items that effect player's statistics at that current time.
 * @param cache The map containing Slot and contents of that slot.
 * @since 1.0.0
 * @author Swofty
 */
public record PlayerItemCache (Map<ItemSlot, SkyblockItem> cache) {

    private static final Map<SkyblockPlayer, PlayerItemCache> playerItemCache = new HashMap<>();

    /**
     * @param player The player which will be looked up in the cache
     * @return The player's item cache
     * @since 1.0.0
     */
    public static PlayerItemCache fromCache(SkyblockPlayer player) {
        return playerItemCache.get(player);
    }

    /**
     * @param player The player which is to be added to item cache.
     * @throws IllegalArgumentException If the cache already contains that player.
     */
    public static void addToCache(SkyblockPlayer player) {
        if (playerItemCache.containsKey(player)) {
            throw new IllegalArgumentException("Skyblock player is already cached");
        } else {
            playerItemCache.put(player, new PlayerItemCache(new ConcurrentHashMap<>()));
            player.updateItemCache();
        }
    }

    /**
     * @param player The player which is to be removed from item cache
     * @throws IllegalArgumentException If the cache does not contain that player
     */
    public static void removefromCache(SkyblockPlayer player) {
        if (playerItemCache.containsKey(player)) {
            playerItemCache.remove(player);
        } else {
            throw new IllegalArgumentException("Skyblock player is not cached");
        }
    }
    
    /**
     * Checks if the specified player is present in the item cache.
     *
     * @param player The player to check in the cache.
     * @return {@code true} if the player is present in the cache, {@code false} otherwise.
     */
    public static boolean contains(SkyblockPlayer player) {
        return playerItemCache.containsKey(player);
    }

    /**
     *
     * @param slot The item slot of the item is to be obtained.
     * @return The skyblock item if present.
     */
    public @NotNull SkyblockItem get(ItemSlot slot) {
        return cache.get(slot);
    }

    /**
     *
     * @param slot The item slot which is to be put in the enum.
     * @param item The Skyblock item which is to be added.
     */
    public void put(ItemSlot slot, SkyblockItem item) {
        cache.put(slot, item);
    }

    /**
     * This method does not guarantee if it has a valid skyblock item.
     * @param slot The item slot to check for.
     * @return If cache contains that slot.
     */
    public boolean contains(ItemSlot slot) {
        return cache.containsKey(slot);
    }

    /**
     * Clears the item contents of cache
     */
    public void clear() {
        cache.clear();
    }

    public Map<ItemSlot, SkyblockItem> getAll() {
        return cache();
    }
}
