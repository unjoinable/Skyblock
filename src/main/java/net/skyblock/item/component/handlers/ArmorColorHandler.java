package net.skyblock.item.component.handlers;

import com.google.gson.JsonObject;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.skyblock.item.component.handlers.trait.StackWriterHandler;
import net.skyblock.item.component.impl.ArmorColorComponent;
import org.jetbrains.annotations.NotNull;

public class ArmorColorHandler implements StackWriterHandler<ArmorColorComponent> {
    private static final String ID = "color";

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
    public void write(@NotNull ArmorColorComponent component, ItemStack.@NotNull Builder builder) {
        int[] color = component.color();
        builder.set(DataComponents.DYED_COLOR, new Color(color[0], color[1], color[2]));
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<ArmorColorComponent> componentType() {
        return ArmorColorComponent.class;
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
    public ArmorColorComponent fromJson(@NotNull JsonObject json) {
        if (json.has(ID)) {
            String colorStr = json.get(ID).getAsString();
            String[] colorParts = colorStr.split(",");

            if (colorParts.length == 3) {
                int[] rgb = new int[3];
                for (int i = 0; i < 3; i++) {
                    rgb[i] = Integer.parseInt(colorParts[i].trim());
                }
                return new ArmorColorComponent(rgb);
            }
        }
        return new ArmorColorComponent(new int[]{0, 0, 0});
    }

}
