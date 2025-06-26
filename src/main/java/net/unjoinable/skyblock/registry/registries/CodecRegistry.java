package net.unjoinable.skyblock.registry.registries;

import net.minestom.server.codec.Codec;
import net.unjoinable.skyblock.item.attribute.impls.BaseStatsAttribute;
import net.unjoinable.skyblock.item.attribute.impls.UpgradedRarityAttribute;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.registry.impl.ImmutableRegistry;

import java.util.Map;

/**
 * A specialized registry for managing codecs associated with item attributes.
 *
 * <p>This registry extends {@link ImmutableRegistry} to provide a type-safe
 * mapping between {@link ItemAttribute} classes and their corresponding
 * {@link Codec} instances used for serialization and deserialization.</p>
 *
 * <p>The registry is immutable once constructed and provides efficient
 * lookup operations for codecs by their associated attribute class types.</p>
 */
public class CodecRegistry extends ImmutableRegistry<Class<? extends ItemAttribute>, Codec<? extends ItemAttribute>> {

    /**
     * Constructs a new CodecRegistry from a map of attribute classes to codecs.
     *
     * <p>The constructor creates an internal registry structure that maps
     * each {@link ItemAttribute} class to its corresponding {@link Codec}.
     * This allows for efficient retrieval of the appropriate codec for
     * serialization/deserialization operations.</p>
     *
     * @param entries the map of {@link ItemAttribute} classes to their
     *                corresponding {@link Codec} instances. Must not be null.
     */
    public CodecRegistry(Map<Class<? extends ItemAttribute>, Codec<? extends ItemAttribute>> entries) {
        super(entries);
    }

    /**
     * Creates a new CodecRegistry with all standard item attribute codecs.
     *
     * <p>This factory method provides a convenient way to create a
     * CodecRegistry pre-populated with codecs for all built-in item
     * attributes. It includes mappings for standard attributes like
     * base stats and rarity upgrades.</p>
     *
     * <p>This is the recommended way to create a CodecRegistry for
     * standard use cases where all default attribute codecs are needed.</p>
     *
     * @return a new {@link CodecRegistry} containing all standard attribute codecs
     */
    public static CodecRegistry withDefaults() {
        return new CodecRegistry(Map.of(
                BaseStatsAttribute.class, BaseStatsAttribute.CODEC,
                UpgradedRarityAttribute.class, UpgradedRarityAttribute.CODEC
        ));
    }
}