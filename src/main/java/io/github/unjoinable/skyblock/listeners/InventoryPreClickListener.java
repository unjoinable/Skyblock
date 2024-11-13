package io.github.unjoinable.skyblock.listeners;

import io.github.unjoinable.skyblock.gui.ClickableItem;
import io.github.unjoinable.skyblock.gui.SkyblockInventory;
import io.github.unjoinable.skyblock.registry.registries.ClickableButtonRegistry;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import io.github.unjoinable.skyblock.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryPreClickListener implements EventListener<InventoryPreClickEvent> {
    private static final Component INVALID_ACTION = StringUtils.toComponent("<red>Invalid GUI action please report.");

    @Override
    public @NotNull Class<InventoryPreClickEvent> eventType() {
        return InventoryPreClickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InventoryPreClickEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        ItemStack item = event.getClickedItem();
        SkyblockInventory inventory = ((SkyblockInventory) event.getInventory());

        //no need to worry about air or tag less
        if (item.isAir() || !item.hasTag(SkyblockInventory.BUTTON_TAG)) return Result.SUCCESS;

        //logic
        try {
            NamespacedId id = NamespacedId.fromString(item.getTag(SkyblockInventory.BUTTON_TAG));
            ClickableItem button = ClickableButtonRegistry.getInstance().get(id);

            button.onClick().accept(player, event);
        } catch (Exception e) {
            player.sendMessage(INVALID_ACTION);
        }

        return Result.SUCCESS;
    }
}
