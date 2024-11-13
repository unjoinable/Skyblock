package io.github.unjoinable.skyblock.gui;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents an item that can be clicked in an inventory GUI.
 */
public interface ClickableItem {

    /**
     * Returns the unique identifier for this clickable item.
     *
     * @return The NamespacedId of this item.
     */
    @NotNull NamespacedId id();

    /**
     * Provides a function to generate the ItemStack for this clickable item.
     *
     * @return A function that takes a SkyblockPlayer and returns an ItemStack.
     */
    @NotNull Function<SkyblockPlayer, ItemStack> item();

    /**
     * Defines the action to be performed when this item is clicked.
     *
     * @return A BiConsumer that takes a SkyblockPlayer and an InventoryPreClickEvent.
     */
    @NotNull BiConsumer<SkyblockPlayer, InventoryPreClickEvent> onClick();

    /**
     * A pre-defined close button that can be used in GUIs.
     * When clicked, it closes the player's inventory.
     */
    Button CLOSE_BUTTON = new Button(
            new NamespacedId("common", "close"),
            (_) -> ItemStack.of(Material.BARRIER)
                    .withCustomName(Component.text("Close", NamedTextColor.RED)
                            .decoration(TextDecoration.ITALIC, false)),
            (player, event) -> {
                event.setCancelled(true);
                player.closeInventory();
            }
    );
}
