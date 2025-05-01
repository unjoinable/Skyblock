package net.skyblock.item.handlers;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.skyblock.item.components.NameComponent;
import net.skyblock.item.handlers.trait.StackWriterHandler;
import org.jetbrains.annotations.NotNull;

public class NameHandler implements StackWriterHandler<NameComponent> {
    private static final String ID = "name";

    /**
     * Writes component-specific data to the provided ItemStack builder.
     * <p>
     * Implementations should modify the builder to include all relevant data
     * from the component this handler is responsible for.
     *
     * @param component the component to write data from
     * @param builder   the ItemStack builder to write component data to
     */
    @Override
    public void write(@NotNull NameComponent component, ItemStack.@NotNull Builder builder) {
        builder.customName(Component.text(component.name()));
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<NameComponent> componentType() {
        return NameComponent.class;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }
}
