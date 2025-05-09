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
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws IllegalAccessError if attempted to be instantiated
     */
    private AttributeResolver() {
        throw new IllegalAccessError("Service Class");
    }

    /**
     * Resolves the category of an item from its attribute container.
     *
     * <p>This method safely extracts the item category from the provided container.
     * If no category attribute is present, it defaults to {@link ItemCategory#NONE}.</p>
     *
     * @param container The attribute container to extract the category from
     * @return The item's category, or ItemCategory.NONE if not found
     */
    public static @NotNull ItemCategory resolveCategory(@NotNull AttributeContainer container) {
        return container.get(ItemCategoryAttribute.class)
                .map(ItemCategoryAttribute::category)
                .orElse(ItemCategory.NONE);
    }

    /**
     * Resolves the rarity of an item from its attribute container.
     *
     * <p>This method safely extracts the item rarity from the provided container,
     * accounting for potential upgrades to the rarity. If the rarity is marked as
     * upgraded, the next higher rarity tier will be returned.</p>
     *
     * @param container The attribute container to extract the rarity from
     * @return The item's rarity (upgraded if applicable), or Rarity.UNOBTAINABLE if not found
     */
    public static @NotNull Rarity resolveRarity(@NotNull AttributeContainer container) {
        return container.get(RarityAttribute.class)
                .map(attribute -> attribute.isUpgraded()
                        ? attribute.rarity().upgrade()
                        : attribute.rarity())
                .orElse(Rarity.UNOBTAINABLE);
    }

    /**
     * Resolves the reforge of an item from its attribute container.
     *
     * <p>This method safely extracts the item reforge from the provided container.
     * Unlike other resolver methods, this one may return null if no reforge is present.</p>
     *
     * @param container The attribute container to extract the reforge from
     * @return The item's reforge, or {@code null} if not found
     */
    public static @Nullable Reforge resolveReforge(@NotNull AttributeContainer container) {
        return container.get(ReforgeAttribute.class)
                .map(ReforgeAttribute::reforge)
                .orElse(null);
    }
}