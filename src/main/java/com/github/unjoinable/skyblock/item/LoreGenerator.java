package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.trait.LoreComponent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generates lore from {@link LoreComponent}s inside a {@link ComponentContainer}.
 * This instance can be reused and optionally cached per item.
 */
public final class LoreGenerator {

    private final ComponentContainer container;

    public LoreGenerator(@NotNull ComponentContainer container) {
        this.container = container;
    }

    public LoreGenerator(@NotNull SkyblockItem item) {
        this.container = item.components();
    }

    /**
     * Generates sorted, combined lore from all applicable components.
     *
     * @return combined lore lines
     */
    public @NotNull List<Component> generate() {
        List<LoreComponent> loreComponents = new ArrayList<>();

        for (com.github.unjoinable.skyblock.item.component.Component comp : container.asMap().values()) {
            if (comp instanceof LoreComponent lore) {
                loreComponents.add(lore);
            }
        }

        // Sort by priority (lower first)
        loreComponents.sort(Comparator.comparingInt(LoreComponent::lorePriority));

        // Collect lore lines
        List<Component> result = new ArrayList<>();

        for (int i = 0; i < loreComponents.size(); i++) {
            result.addAll(loreComponents.get(i).generateLore(container));
           if (i != loreComponents.size() - 1) {
               result.add(Component.empty());
           }
        }
        return result;
    }
}

