package io.github.unjoinable.skyblock.gui;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import io.github.unjoinable.skyblock.util.NamespacedObject;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents a button in a GUI.
 *
 * @param id The unique identifier of the button.
 * @param item The item displayed on the button.
 * @param task The action to be performed when the button is clicked.
 */
public record Button(@NotNull NamespacedId id,
                     @NotNull Function<SkyblockPlayer, ItemStack> item,
                     @NotNull BiConsumer<SkyblockPlayer, InventoryPreClickEvent> task
                     ) implements NamespacedObject, ClickableItem {

    @Override
    public @NotNull BiConsumer<SkyblockPlayer, InventoryPreClickEvent> onClick() {
        return task;
    }
}
