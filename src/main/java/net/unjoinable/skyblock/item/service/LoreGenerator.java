package net.unjoinable.skyblock.item.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.LoreAttribute;
import net.unjoinable.skyblock.item.enums.ItemCategory;
import net.unjoinable.skyblock.item.enums.Rarity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.TextDecoration.*;

public class LoreGenerator {
    private final AttributeContainer container;
    private final ItemMetadata metadata;

    /**
     * Initializes a LoreGenerator using the provided attribute container and item metadata.
     *
     * @param container the attribute container from which lore attributes will be sourced
     * @param metadata  the metadata from which essential info will be sourced
     */
    public LoreGenerator(AttributeContainer container, ItemMetadata metadata) {
        this.container = container;
        this.metadata = metadata;
    }

    /**
     * Generates a combined list of lore components from all `ItemLoreAttribute` instances in the container, sorted by priority.
     *
     * @return a list of lore lines as Components, ordered by attribute priority
     */
    public List<Component> generate() {
        List<LoreAttribute> attributes = container.stream()
                .filter(LoreAttribute.class::isInstance)
                .map(LoreAttribute.class::cast)
                .sorted(Comparator.comparingInt(LoreAttribute::priority))
                .toList();
        return generateCombinedLore(attributes);
    }

    /**
     * Merges lore lines from each attribute into a single list, inserting an empty line between non-empty sections.
     *
     * @param attributes the sorted list of lore attributes to combine
     * @return a list of lore components with spacing between attribute sections
     */
    private List<Component> generateCombinedLore(List<LoreAttribute> attributes) {
        List<Component> result = new ArrayList<>(metadata.description());

        for (int i = 0; i < attributes.size(); i++) {
            List<Component> attrLore = attributes.get(i).loreLines(container, metadata);

            if (!attrLore.isEmpty()) {
                result.addAll(attrLore);

                // Add spacing between sections except after the last one
                if (i < attributes.size() - 1) {
                    result.add(Component.empty());
                }
            }
        }
        result.add(Component.empty());
        result.add(formatRarity(metadata.rarity(), metadata.category(), AttributeResolver.isRarityUpgraded(container)));
        return result;
    }

    /**
     * Formats item rarity and category with color and styling.
     * Upgraded items get obfuscated borders: "§ka§r EPIC SWORD §ka§r"
     *
     * @param rarity base rarity before upgrades
     * @param category item category (SWORD, BOW, etc.)
     * @param isUpgraded whether rarity was upgraded
     * @return styled component for display
     */
    private static Component formatRarity(Rarity rarity, ItemCategory category, boolean isUpgraded) {
        Rarity itemRarity = isUpgraded ? rarity.upgrade() : rarity;
        TextColor color = itemRarity.color();
        String categoryName = category.displayName();

        Component base = text(itemRarity.name() + " " + categoryName, color, BOLD)
                .decoration(ITALIC, false);

        return isUpgraded
                ? textOfChildren(text("a ", color, OBFUSCATED), base, text(" a", color, OBFUSCATED))
                .decoration(ITALIC, false)
                : base;
    }
}
