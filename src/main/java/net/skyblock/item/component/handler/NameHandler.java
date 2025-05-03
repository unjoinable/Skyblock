package net.skyblock.item.component.handler;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.skyblock.item.definition.Reforge;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.trait.StackWriterHandler;
import net.skyblock.item.component.definition.NameComponent;
import net.skyblock.item.component.service.ComponentResolver;
import net.skyblock.item.enums.Rarity;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

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
     * @param container the container containing all components
     */
    @Override
    public void write(@NotNull NameComponent component, ItemStack.@NotNull Builder builder, @NotNull ComponentContainer container) {
        ComponentResolver resolver = new ComponentResolver();
        Rarity rarity = resolver.resolveRarity(container);
        Reforge reforge = resolver.resolveReforge(container);
        String name = component.name();

        if (reforge != null) {
            name = reforge.reforgeId() + " " + name;
        }

        builder.customName(Component.text(name, rarity.getColor()).decoration(ITALIC, false));
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

    /**
     * Creates a component instance from JSON data
     *
     * @param json The JSON data to parse
     * @return The created component instance
     * @throws UnsupportedOperationException by default unless overridden
     */
    @Override
    public NameComponent fromJson(@NotNull JsonElement json) {
        String textureValue = json.getAsString();
        return new NameComponent(textureValue);
    }
}
