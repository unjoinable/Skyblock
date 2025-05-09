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
     * Creates a LoreGenerator with the specified container.
     */
    public LoreGenerator(@NotNull AttributeContainer container) {
        this.container = container;
    }

    /**
     * Creates a LoreGenerator using a SkyblockItem's attributes.
     */
    public LoreGenerator(@NotNull SkyblockItem item) {
        this(item.attributes());
    }

    /**
     * Generates sorted, combined lore from all applicable attributes.
     *
     * @return Combined lore lines as Components
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
     * Combines lore from the sorted attributes with spacing between sections.
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