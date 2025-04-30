package net.skyblock.item.handlers;

import net.minestom.server.item.ItemStack;
import net.skyblock.item.components.MaterialComponent;
import org.jetbrains.annotations.NotNull;

public class MaterialHandler implements StackWriterHandler<MaterialComponent> {
    private static final String ID = "skyblock:material_handler";

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
    public void write(@NotNull MaterialComponent component, ItemStack.@NotNull Builder builder) {
        builder.material(component.material());
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<MaterialComponent> componentType() {
        return MaterialComponent.class;
    }

    /**
     * Returns the unique identifier for this component type
     */
    @Override
    public @NotNull String componentId() {
        return ID;
    }
}
