package net.skyblock.item.io;

import net.kyori.adventure.text.Component;
import net.skyblock.item.attribute.AttributeContainer;
import net.skyblock.item.attribute.base.ItemLoreAttribute;
import net.skyblock.item.definition.SkyblockItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generates lore from ItemLoreAttributes in an AttributeContainer.
 */
public final class LoreGenerator {
    private final AttributeContainer container;

    /**
     * Initializes a LoreGenerator using the provided attribute container.
     *
     * @param container the attribute container from which lore attributes will be sourced
     */
    public LoreGenerator(@NotNull AttributeContainer container) {
        this.container = container;
    }

    /**
     * Constructs a LoreGenerator using the attributes from the specified SkyblockItem.
     *
     * @param item the SkyblockItem whose attributes will be used for lore generation
     */
    public LoreGenerator(@NotNull SkyblockItem item) {
        this(item.attributes());
    }

    /**
     * Generates a combined list of lore components from all `ItemLoreAttribute` instances in the container, sorted by priority.
     *
     * @return a list of lore lines as Components, ordered by attribute priority
     */
    public @NotNull List<Component> generate() {
        List<ItemLoreAttribute> attributes = container.stream()
                .filter(ItemLoreAttribute.class::isInstance)
                .map(ItemLoreAttribute.class::cast)
                .sorted(Comparator.comparingInt(ItemLoreAttribute::priority))
                .toList();

        return generateCombinedLore(attributes);
    }

    /**
     * Merges lore lines from each attribute into a single list, inserting an empty line between non-empty sections.
     *
     * @param attributes the sorted list of lore attributes to combine
     * @return a list of lore components with spacing between attribute sections
     */
    private @NotNull List<Component> generateCombinedLore(@NotNull List<ItemLoreAttribute> attributes) {
        List<Component> result = new ArrayList<>();

        for (int i = 0; i < attributes.size(); i++) {
            List<Component> attrLore = attributes.get(i).loreLines(container);

            if (!attrLore.isEmpty()) {
                result.addAll(attrLore);

                // Add spacing between sections except after the last one
                if (i < attributes.size() - 1) {
                    result.add(Component.empty());
                }
            }
        }

        return result;
    }
}