package net.skyblock.item.component.definition;

import net.skyblock.item.definition.Reforge;
import net.skyblock.item.component.ModifierComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A component that represents a reforge applied to an item.
 * <p>
 * In Skyblock, reforges are special modifiers that can be applied to items
 * to enhance their stats. This component tracks which reforge, if any,
 * is currently applied to an item.
 * </p>
 *
 * @param reforge The current forge, null if none applied.
 * @see Reforge
 * @see ModifierComponent
 */
public record ReforgeComponent(@Nullable Reforge reforge) implements ModifierComponent {

    /**
     * Creates an empty reforge component with no reforge applied.
     *
     * @return A new {@code ReforgeComponent} instance with {@code null} reforge
     */
    public static @NotNull ReforgeComponent empty() {
        return new ReforgeComponent(null);
    }

    /**
     * Checks if this component has a reforge applied.
     *
     * @return {@code true} if a reforge is present, {@code false} otherwise
     */
    public boolean hasReforge() {
        return reforge != null;
    }
}