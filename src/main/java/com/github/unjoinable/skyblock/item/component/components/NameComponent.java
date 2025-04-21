package com.github.unjoinable.skyblock.item.component.components;

import com.github.unjoinable.skyblock.item.component.trait.SerializableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A final class representing the name component of an item.
 * This class defines the display name for an item and implements SerializableComponent.
 */
public final class NameComponent implements SerializableComponent {
    private final String displayName;

    /**
     * Constructs a NameComponent with the specified display name.
     * @param displayName The display name of the item.
     */
    public NameComponent(@NotNull String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retrieves the display name of the item.
     * @return The display name as a {@link String}.
     */
    public @NotNull String getDisplayName() {
        return displayName;
    }

    @Override
    public void nbtWriter(ItemStack.@NotNull Builder builder) {
        builder.customName(Component.text(displayName)
                .decoration(TextDecoration.ITALIC, false));
    }
}