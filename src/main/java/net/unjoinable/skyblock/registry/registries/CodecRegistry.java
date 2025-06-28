package net.unjoinable.skyblock.registry.registries;

import net.minestom.server.codec.Codec;
import net.unjoinable.skyblock.item.attribute.impls.AbilityAttribute;
import net.unjoinable.skyblock.item.attribute.impls.BaseStatsAttribute;
import net.unjoinable.skyblock.item.attribute.impls.UpgradedRarityAttribute;
import net.unjoinable.skyblock.item.attribute.traits.ItemAttribute;
import net.unjoinable.skyblock.registry.impl.PairedKeyRegistry;
import net.unjoinable.skyblock.utils.NamespaceId;

import java.util.Map;
import java.util.Optional;

/**
 * A specialized registry for managing codecs associated with item attributes.
 *
 * <p>This registry extends {@link PairedKeyRegistry} to provide a type-safe
 * mapping between {@link ItemAttribute} classes and their corresponding
 * {@link Codec} instances, with additional lookup capability by {@link NamespaceId}.</p>
 *
 * <p>The registry supports dual-key access:
 * <ul>
 *   <li>Primary key: {@link ItemAttribute} class type</li>
 *   <li>Secondary key: {@link NamespaceId} for namespace-based lookups</li>
 * </ul></p>
 *
 * <p>This allows for efficient lookup operations for codecs by either their
 * associated attribute class types or their namespace identifiers.</p>
 */
public class CodecRegistry extends PairedKeyRegistry<Class<? extends ItemAttribute>, NamespaceId, Codec<? extends ItemAttribute>> {

    /**
     * Constructs a new CodecRegistry.
     *
     * <p>Creates an empty registry that can be populated with attribute
     * classes, namespace IDs, and their corresponding codecs.</p>
     */
    public CodecRegistry() {
        super();
    }

    /**
     * Constructs a new CodecRegistry with custom map implementations.
     *
     * <p>This constructor allows for custom backing map implementations
     * to be used for the primary and secondary key storage.</p>
     *
     * @param primaryMap   the map to use for class-to-codec storage
     * @param secondaryMap the map to use for namespace-to-class mapping
     */
    public CodecRegistry(Map<Class<? extends ItemAttribute>, Codec<? extends ItemAttribute>> primaryMap,
                         Map<NamespaceId, Class<? extends ItemAttribute>> secondaryMap) {
        super(primaryMap, secondaryMap);
    }

    /**
     * Register a codec with both its attribute class and namespace ID.
     *
     * <p>This method allows registration of a codec that can be retrieved
     * by either the attribute class type or the namespace identifier.</p>
     *
     * @param attributeClass the {@link ItemAttribute} class type
     * @param namespaceId    the {@link NamespaceId} for the attribute
     * @param codec          the {@link Codec} for serialization/deserialization
     */
    public void registerCodec(Class<? extends ItemAttribute> attributeClass,
                              NamespaceId namespaceId,
                              Codec<? extends ItemAttribute> codec) {
        register(attributeClass, namespaceId, codec);
    }

    /**
     * Get a codec by its namespace ID.
     *
     * <p>This method allows retrieval of codecs using namespace identifiers,
     * providing an alternative lookup mechanism for when the exact class
     * type is not readily available.</p>
     *
     * @param namespaceId the {@link NamespaceId} to look up
     * @return an {@link Optional} containing the codec if found, empty otherwise
     */
    public Optional<Codec<? extends ItemAttribute>> getCodecByNamespace(NamespaceId namespaceId) {
        return getBySecondaryKey(namespaceId);
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
     * <p><strong>Note:</strong> You'll need to provide the appropriate
     * {@link NamespaceId} constants for each attribute type.</p>
     *
     * @return a new {@link CodecRegistry} containing all standard attribute codecs
     */
    public static CodecRegistry withDefaults() {
        CodecRegistry registry = new CodecRegistry();
        registry.registerCodec(BaseStatsAttribute.class, BaseStatsAttribute.ID, BaseStatsAttribute.CODEC);
        registry.registerCodec(UpgradedRarityAttribute.class, UpgradedRarityAttribute.ID, UpgradedRarityAttribute.CODEC);
        registry.registerCodec(AbilityAttribute.class, AbilityAttribute.ID, AbilityAttribute.CODEC);
        return registry;
    }
}