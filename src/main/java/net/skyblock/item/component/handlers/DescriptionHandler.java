package net.skyblock.item.component.handlers;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.trait.LoreHandler;
import net.skyblock.item.component.impl.DescriptionComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class DescriptionHandler implements LoreHandler<DescriptionComponent> {
    private static final MiniMessage miniMessage = MiniMessage
            .builder().postProcessor(component -> component.decoration(ITALIC, false)).build();
    private static final String ID = "description";
    /**
     * Priority value to determine lore ordering.
     * Lower = appears earlier in the item lore.
     *
     * @return the priority value for sorting
     */
    @Override
    public int lorePriority() {
        return 50;
    }

    /**
     * Generates lore lines for this component.
     *
     * @param component the component to generate lore for
     * @param container the full component container, in case this lore depends on other components
     * @return list of components representing lore lines
     */
    @Override
    public @NotNull List<Component> generateLore(@NotNull DescriptionComponent component, @NotNull ComponentContainer container) {
        return component.description();
    }

    /**
     * Returns the component type this handler is responsible for
     */
    @Override
    public @NotNull Class<DescriptionComponent> componentType() {
        return DescriptionComponent.class;
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
    public DescriptionComponent fromJson(@NotNull JsonElement json) {
        List<Component> description = new ArrayList<>();

        if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                String value = element.getAsString();
                Component component = miniMessage.deserialize(value);
                description.add(component);
            }
        }
        return new DescriptionComponent(description);
    }
}
