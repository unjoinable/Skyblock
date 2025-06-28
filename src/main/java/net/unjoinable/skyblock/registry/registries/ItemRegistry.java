package net.unjoinable.skyblock.registry.registries;

import net.kyori.adventure.key.Key;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.service.ItemLoader;
import net.unjoinable.skyblock.registry.impl.ImmutableRegistry;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A specialized registry for managing Skyblock items.
 *
 * <p>This registry extends {@link ImmutableRegistry} to provide a type-safe
 * container for {@link SkyblockItem} instances, indexed by their
 * {@link Key} keys.</p>
 *
 * <p>The registry is immutable once constructed and provides efficient
 * lookup operations for items by their keys.</p>
 */
public class ItemRegistry extends ImmutableRegistry<Key, SkyblockItem> {

    /**
     * Constructs a new ItemRegistry from a list of SkyblockItem instances.
     *
     * <p>The constructor processes the provided list of items and creates
     * an internal map structure where each item is indexed by its metadata
     * identifier. This allows for efficient O(1) lookup operations.</p>
     *
     * @param entries the list of {@link SkyblockItem} instances to register.
     *                Must not be null. Items with duplicate IDs will result
     *                in only the last item being retained in the registry.
     */
    public ItemRegistry(List<SkyblockItem> entries) {
        super(entries
                .stream()
                .collect(Collectors.toMap(
                        item -> item.metadata().key(),
                        Function.identity()
                )));
    }

    /**
     * Creates a new ItemRegistry by loading all available Skyblock items.
     *
     * <p>This factory method provides a convenient way to create a fully
     * populated ItemRegistry without manually managing the item loading
     * process. It uses an {@link ItemLoader} to discover and load all
     * available items from the configured data sources.</p>
     *
     * <p>This is the recommended way to create an ItemRegistry for
     * production use, as it ensures all items are properly loaded
     * and registered.</p>
     *
     * @return a new {@link ItemRegistry} containing all loaded items
     *
     * @see ItemLoader#loadItems()
     */
    public static ItemRegistry withDefaults() {
        ItemLoader loader = new ItemLoader();
        return new ItemRegistry(loader.loadItems());
    }
}