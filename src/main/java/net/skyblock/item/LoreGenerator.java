package net.skyblock.item;

import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.trait.LoreComponent;
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

    /**
     * Constructor to create a LoreGenerator with a specific {@link ComponentContainer}.
     * <p>
     * This constructor allows you to create a LoreGenerator instance that can generate lore based on
     * the provided container, which holds various components that might contribute lore.
     * </p>
     *
     * @param container The {@link ComponentContainer} that holds the components used to generate lore.
     */

    public LoreGenerator(@NotNull ComponentContainer container) {
        this.container = container;
    }

    /**
     * Constructor to create a LoreGenerator using a {@link SkyblockItem}.
     * <p>
     * This constructor creates a LoreGenerator instance based on the components of the provided
     * {@link SkyblockItem}. The item’s components are passed to the container, which is then used to
     * generate the lore.
     * </p>
     *
     * @param item The {@link SkyblockItem} from which the components are extracted for lore generation.
     */

    public LoreGenerator(@NotNull SkyblockItem item) {
        this(item.components());
    }

    /**
     * Generates sorted, combined lore from all applicable components.
     *
     * @return combined lore lines
     */
    public @NotNull List<Component> generate() {
        List<LoreComponent> loreComponents = new ArrayList<>();

        for (ItemComponent comp : container.asMap().values()) {
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

