package net.unjoinable.skyblock.item.service;

import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.impls.UpgradedRarityAttribute;
import net.unjoinable.skyblock.item.attribute.traits.StatModifierAttribute;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Utility class for resolving and querying item attribute states.
 * <p>
 * This class provides static helper methods to extract meaningful information
 * from {@link AttributeContainer} instances without exposing the underlying
 * attribute implementation details to client code.
 * <p>
 * The resolver pattern centralizes attribute queries and provides a stable API
 * that can evolve independently of the attribute system's internal structure.
 * This makes it easier to refactor attribute implementations or add caching
 * without breaking existing code.
 */
public class AttributeResolver {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This is a utility class that should only be used through its static methods.
     *
     * @throws AssertionError always, to prevent instantiation
     */
    private AttributeResolver() {
        throw new AssertionError("AttributeResolver is a utility class and should not be instantiated");
    }

    /**
     * Determines whether an item's rarity has been upgraded from its base value.
     * <p>
     * This method safely queries the {@link UpgradedRarityAttribute} within the
     * provided container and returns whether the rarity upgrade flag is set.
     * If the attribute is not present, this method assumes the rarity has not
     * been upgraded.
     *
     * @param container the attribute container to query (must not be null)
     * @return {@code true} if the item's rarity has been upgraded from its base value,
     *         {@code false} if the rarity is unchanged or the upgrade attribute is not present
     */
    public static boolean isRarityUpgraded(AttributeContainer container) {
        return container
                .get(UpgradedRarityAttribute.class)
                .map(UpgradedRarityAttribute::isUpgraded)
                .orElse(false);
    }

    /**
     * Retrieves all stat modifier attributes from the given container.
     * <p>
     * This method streams through all attributes in the container and filters
     * for those that implement the {@link StatModifierAttribute} interface.
     * The returned stream can be used to process stat modifiers in a functional
     * style, allowing for operations like mapping, filtering, or collecting.
     * <p>
     * If no stat modifier attributes are present in the container, an empty
     * stream is returned. The stream is lazily evaluated, so filtering and
     * casting operations are only performed when terminal operations are invoked.
     *
     * @param container the attribute container to query (must not be null)
     * @return a stream of all {@link StatModifierAttribute} instances found
     *         in the container, or an empty stream if none are present
     * @see StatModifierAttribute
     */
    public static Stream<StatModifierAttribute> getStatModifiers(AttributeContainer container) {
        return container
                .stream()
                .filter(StatModifierAttribute.class::isInstance)
                .map(StatModifierAttribute.class::cast)
                .sorted(Comparator.comparingInt(StatModifierAttribute::modifierPriority));
    }
}