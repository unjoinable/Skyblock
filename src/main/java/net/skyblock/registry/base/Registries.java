package net.skyblock.registry.base;

import net.skyblock.registry.impl.HandlerRegistry;
import net.skyblock.registry.impl.ItemRegistry;
import net.skyblock.registry.impl.ReforgeRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * Interface providing convenient access to commonly used registries.
 */
public interface Registries {
    /**
     * Gets the item registry
     *
     * @return The item registry
     */
    @NotNull ItemRegistry items();

    /**
     * Gets the handler registry
     *
     * @return The handler registry
     */
    @NotNull HandlerRegistry handlers();

    /**
     * Gets the reforge registry
     *
     * @return The reforge registry
     */
    @NotNull ReforgeRegistry reforges();
}