package net.skyblock.item.component.handlers;

import com.google.gson.JsonElement;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.skyblock.item.component.impl.MaterialComponent;
import net.skyblock.item.component.handlers.trait.StackWriterHandler;
import org.jetbrains.annotations.NotNull;

public class MaterialHandler implements StackWriterHandler<MaterialComponent> {
    private static final String ID = "material";

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

    /**
     * Creates a component instance from JSON data
     *
     * @param json The JSON data to parse
     * @return The created component instance
     */
    @Override
    public MaterialComponent fromJson(@NotNull JsonElement json) {
        String value = json.getAsString();
        Material material = Material.fromKey(value.toLowerCase());
        return new MaterialComponent(material);

    }
}
