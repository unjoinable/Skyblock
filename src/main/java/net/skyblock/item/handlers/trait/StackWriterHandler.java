package net.skyblock.item.handlers.trait;

import net.minestom.server.item.ItemStack;
import net.skyblock.item.ItemComponentHandler;
import net.skyblock.item.component.ItemComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Handler interface for writing component-specific data to an ItemStack builder.
 * <p>
 * This handler is responsible for transferring data from a specific ItemComponent type
 * to an ItemStack that's being constructed. It's part of the component-based item system
 * that allows modular handling of different item aspects.
 *
 * @param <C> the specific type of ItemComponent this handler works with
 */
public interface StackWriterHandler<C extends ItemComponent> extends ItemComponentHandler<C> {

    /**
     * Writes component-specific data to the provided ItemStack builder.
     * <p>
     * Implementations should modify the builder to include all relevant data
     * from the component this handler is responsible for.
     *
     * @param component the component to write data from
     * @param builder   the ItemStack builder to write component data to
     */
    void write(@NotNull C component, @NotNull ItemStack.Builder builder);
}