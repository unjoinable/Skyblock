package net.skyblock.item.component.event;

import net.skyblock.item.component.ComponentContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for receiving notifications when Skyblock items are loaded.
 * Extends the ComponentChangeListener to provide additional functionality
 * specific to the item loading process.
 */
public interface SkyblockItemLoadListener  {

    /**
     * Called when a Skyblock item is loaded.
     * This method can be used to perform any modifications or actions
     * needed when an item is initially loaded into the system.
     *
     * @param container The component container associated with the item
     * @return The possibly modified container
     */
    @NotNull ComponentContainer onItemLoad(ComponentContainer container);
}