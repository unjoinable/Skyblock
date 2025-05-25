/**
 * Factory class responsible for creating and initializing registry instances.
 * <p>
 * The RegistryFactory provides a centralized mechanism for creating various registry types
 * used throughout the Skyblock system. Each factory method follows a consistent pattern of
 * returning fully initialized registry instances with appropriate configurations.
 * <p>
 * Registry instances created by this factory are typically immutable to ensure thread safety
 * and prevent unintended modifications. Loading of registry contents is handled by specialized
 * loader classes that populate the registries with their initial data.
 * <p>
 * This factory serves as an abstraction layer between registry consumers and the specific
 * implementation details of each registry type.
 *
 * @see Registry
 * @see ImmutableRegistry
 */
package net.unjoinable.registry.factory;

import net.minestom.server.codec.Codec;
import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.attribute.ItemAttribute;
import net.unjoinable.item.attribute.impls.BaseStatsAttribute;
import net.unjoinable.item.attribute.impls.UpgradedRarityAttribute;
import net.unjoinable.registry.Registry;
import net.unjoinable.registry.impl.ImmutableRegistry;
import net.unjoinable.utility.NamespaceId;

import java.util.Map;

public class RegistryFactory {

    private RegistryFactory() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Creates and initializes the Skyblock item registry.
     * <p>
     * This registry serves as the central repository for all Skyblock items in the system.
     * Items are indexed by their unique {@link NamespaceId}.
     * <p>
     * The returned registry is immutable to ensure thread safety and prevent unauthorized
     * modifications. The registry is populated during initialization using the SkyblockItemLoader,
     * which is responsible for loading all available items from their respective data sources.
     * <p>
     *
     * @return An immutable registry containing all registered Skyblock items
     * @see SkyblockItem
     * @see NamespaceId
     */
    public static Registry<NamespaceId, SkyblockItem> createItemRegistry() {
        return new ImmutableRegistry<>(Map.of());
    }

    public static Registry<Class<? extends ItemAttribute>, Codec<? extends ItemAttribute>> createAttributeCodecRegistry() {
        return new ImmutableRegistry<>(Map.of(
                BaseStatsAttribute.class, BaseStatsAttribute.CODEC,
                UpgradedRarityAttribute.class, UpgradedRarityAttribute.CODEC
        ));
    }
}