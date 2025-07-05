package net.unjoinable.skyblock.event.listener.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.ui.inventory.ItemSlot;

import java.util.function.Consumer;

import static net.unjoinable.skyblock.event.listener.player.PlayerListenerConstants.ARMOR_SLOT_MAP;

/**
 * Handles player item use events, armor equipping, and ability usage.
 */
public class PlayerUseItemListener implements Consumer<PlayerUseItemEvent> {

    @Override
    public void accept(PlayerUseItemEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        ItemStack itemStack = event.getItemStack();
        Material material = itemStack.material();
        ItemSlot slot = ARMOR_SLOT_MAP.get(material);

        if (slot != null) {
            MinecraftServer.getSchedulerManager().scheduleEndOfTick(() -> player.getStatSystem().updateSlot(slot));
            return;
        }

        player.getAbilitySystem().tryUse(itemStack, ExecutionType.RIGHT_CLICK);
    }
}