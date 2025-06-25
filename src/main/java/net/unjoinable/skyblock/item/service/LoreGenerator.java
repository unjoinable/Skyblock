package net.unjoinable.skyblock.item.service;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.item.ItemMetadata;
import net.unjoinable.skyblock.item.attribute.AttributeContainer;
import net.unjoinable.skyblock.item.attribute.traits.LoreAttribute;

import java.util.ArrayList;
import java.util.List;

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
