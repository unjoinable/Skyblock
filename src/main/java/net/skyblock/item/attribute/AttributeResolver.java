package net.skyblock.item.attribute;

import net.skyblock.item.attribute.impl.ItemCategoryAttribute;
import net.skyblock.item.attribute.impl.RarityAttribute;
import net.skyblock.item.attribute.impl.ReforgeAttribute;
import net.skyblock.item.definition.Reforge;
import net.skyblock.item.enums.ItemCategory;
import net.skyblock.item.enums.Rarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service for resolving attributes from attribute containers.
 *
 * <p>This utility class provides static methods to retrieve common attributes from
 * item attribute containers in a standardized way with appropriate fallback values.</p>
 *
 * <p>All methods in this class handle the case when an attribute is not present by
 * returning sensible default values.</p>
 */
public final class AttributeResolver {

    /**
     * Prevents instantiation of the {@code AttributeResolver} utility class.
     *
     * @throws IllegalAccessError always thrown to enforce non-instantiability
     */
    private AttributeResolver() {
        throw new IllegalAccessError("Service Class");
    }

    /**
     * Returns the item category from the given attribute container, or {@link ItemCategory#NONE} if not present.
     *
     * @param container the attribute container to query
     * @return the resolved item category, or {@code ItemCategory.NONE} if absent
     */
    public static @NotNull ItemCategory resolveCategory(@NotNull AttributeContainer container) {
        return container.get(ItemCategoryAttribute.class)
                .map(ItemCategoryAttribute::category)
                .orElse(ItemCategory.NONE);
    }

    /**
     * Returns the item's rarity from the given attribute container, applying an upgrade if specified.
     *
     * If the container has a rarity attribute and it is marked as upgraded, the next higher rarity tier is returned.
     * If no rarity attribute is present, {@code Rarity.UNOBTAINABLE} is returned.
     *
     * @param container the attribute container to query
     * @return the item's rarity, upgraded if applicable, or {@code Rarity.UNOBTAINABLE} if not found
     */
    public static @NotNull Rarity resolveRarity(@NotNull AttributeContainer container) {
        return container.get(RarityAttribute.class)
                .map(attribute -> attribute.isUpgraded()
                        ? attribute.rarity().upgrade()
                        : attribute.rarity())
                .orElse(Rarity.UNOBTAINABLE);
    }

    /**
     * Retrieves the item's reforge from the given attribute container.
     *
     * @param container the attribute container to query
     * @return the item's reforge, or {@code null} if no reforge attribute is present
     */
    public static @Nullable Reforge resolveReforge(@NotNull AttributeContainer container) {
        return container.get(ReforgeAttribute.class)
                .map(ReforgeAttribute::reforge)
                .orElse(null);
    }
}