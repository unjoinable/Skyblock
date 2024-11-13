package io.github.unjoinable.skyblock.listeners;

import io.github.unjoinable.skyblock.gui.Button;
import io.github.unjoinable.skyblock.gui.InventoryGUI;
import io.github.unjoinable.skyblock.registry.registries.ButtonRegistry;
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

        //no need to worry about air
        if (item.isAir()) return Result.SUCCESS;

        //logic
        try {
            NamespacedId id = NamespacedId.fromString(item.getTag(InventoryGUI.BUTTON_TAG));
            Button button = ButtonRegistry.getInstance().get(id);
            //button.task().accept()
            
        } catch (Exception e) {
            player.sendMessage(INVALID_ACTION);
        }


        return Result.SUCCESS;
    }
}
