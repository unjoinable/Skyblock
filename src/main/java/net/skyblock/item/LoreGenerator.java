package net.skyblock.item;

import net.kyori.adventure.text.Component;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.ItemComponent;
import net.skyblock.item.component.handlers.trait.LoreHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Generates lore from LoreComponents inside a {@link ComponentContainer}.
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
     * {@link SkyblockItem}. The item's components are passed to the container, which is then used to
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
        // Collect all components with LoreHandler
        List<ComponentWithHandler> loreComponents = collectLoreComponents();

        // Sort by priority
        loreComponents.sort(Comparator.comparingInt(comp -> comp.handler.lorePriority()));

        // Generate combined lore
        return generateCombinedLore(loreComponents);
    }

    /**
     * Collects all components that have a LoreHandler from the container.
     *
     * @return list of components paired with their handlers
     */
    private @NotNull List<ComponentWithHandler> collectLoreComponents() {
        List<ComponentWithHandler> result = new ArrayList<>();

        for (ItemComponent component : container.asMap().values()) {
            if (component.getHandler() instanceof LoreHandler) {
                result.add(new ComponentWithHandler(component, (LoreHandler<?>) component.getHandler()));
            }
        }

        return result;
    }

    /**
     * Generates the combined lore from sorted components.
     *
     * @param components sorted list of components with handlers
     * @return combined list of lore lines
     */
    private @NotNull List<Component> generateCombinedLore(@NotNull List<ComponentWithHandler> components) {
        List<Component> result = new ArrayList<>();

        for (int i = 0; i < components.size(); i++) {
            ComponentWithHandler pair = components.get(i);

            // Here we use the safe type-erased bridge method from LoreHandler
            result.addAll(generateLoreFromHandler(pair.component, pair.handler, container));

            // Add spacing between component lore sections
            if (i != components.size() - 1) {
                result.add(Component.empty());
            }
        }

        return result;
    }

    /**
     * Helper method to safely generate lore from a handler with proper type checking.
     * This bridges the gap between the generic and non-generic types.
     *
     * @param component the component to generate lore for
     * @param handler the handler that processes the component
     * @param container the component container
     * @return the generated lore lines
     */
    @SuppressWarnings("unchecked")
    private static <C extends ItemComponent> @NotNull List<Component> generateLoreFromHandler(
            @NotNull ItemComponent component,
            @NotNull LoreHandler<C> handler,
            @NotNull ComponentContainer container) {

        // Verify the component type is compatible with the handler
        Class<C> componentClass = handler.componentType();
        if (!componentClass.isInstance(component)) {
            throw new IllegalArgumentException("Component type mismatch: expected " +
                    componentClass.getName() + " but got " + component.getClass().getName());
        }

        // Safe cast because we checked the type
        return handler.generateLore((C) component, container);
    }


    /**
     * Helper class to pair a component with its handler
     */
    private record ComponentWithHandler(ItemComponent component, LoreHandler<?> handler) {}
}