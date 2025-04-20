package com.github.unjoinable.skyblock.item;

import com.github.unjoinable.skyblock.item.component.Component;
import com.github.unjoinable.skyblock.item.component.ComponentContainer;
import com.github.unjoinable.skyblock.item.component.trait.LoreComponent;
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
    public @NotNull List<net.kyori.adventure.text.Component> generate() {
        List<LoreComponent> loreComponents = new ArrayList<>(4);

        for (Component comp : container.asMap().values()) {
            if (comp instanceof LoreComponent lore) {
                loreComponents.add(lore);
            }
        }

        // Sort by priority (lower first)
        loreComponents.sort(Comparator.comparingInt(LoreComponent::lorePriority));

        // Collect lore lines
        List<net.kyori.adventure.text.Component> result = new ArrayList<>(loreComponents.size() * 2);
        for (LoreComponent lore : loreComponents) {
            result.addAll(lore.generateLore(container));
        }

        return result;
    }
}

