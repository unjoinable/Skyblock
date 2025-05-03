package net.skyblock.event.listeners.item;

import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.event.SkyblockItemLoadListener;
import net.skyblock.item.component.definition.ReforgeComponent;
import net.skyblock.item.component.service.ComponentResolver;
import net.skyblock.item.enums.ItemCategory;
import org.jetbrains.annotations.NotNull;

public class ItemLoadListenerImpl implements SkyblockItemLoadListener {

    /**
     * Called when a Skyblock item is loaded.
     * This method can be used to perform any modifications or actions
     * needed when an item is initially loaded into the system.
     *
     * @param container The component container associated with the item
     * @return The possibly modified container
     */
    @Override
    public @NotNull ComponentContainer onItemLoad(ComponentContainer container) {
        ComponentResolver resolver = new ComponentResolver();
        ItemCategory category = resolver.resolveCategory(container);

        if (category.isReforgeable()) {
            container = container.with(ReforgeComponent.empty());
        }
        return container;
    }

}
