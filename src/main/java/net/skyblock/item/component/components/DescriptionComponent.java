package net.skyblock.item.component.components;

import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.trait.LoreComponent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * A class representing a description component for items.
 */
public final class DescriptionComponent implements LoreComponent {
    private final List<Component> description;

    /**
     * Constructs a DescriptionComponent with the specified description.
     * @param description A list of {@link Component} objects representing the description lines.
     */
    public DescriptionComponent(@NotNull List<Component> description) {
        this.description = Collections.unmodifiableList(description);
    }

    /**
     * Gets the description associated with this component.
     * @return A list of {@link Component} objects representing the description lines.
     */
    public @NotNull List<Component> getDescription() {
        return description;
    }

    @Override
    public int lorePriority() {
        return 50;
    }

    @Override
    public @NotNull List<Component> generateLore(@NotNull ComponentContainer container) {
        return description;
    }
}