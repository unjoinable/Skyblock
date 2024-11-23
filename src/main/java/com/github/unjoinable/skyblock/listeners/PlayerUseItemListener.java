package com.github.unjoinable.skyblock.listeners;

import com.github.unjoinable.skyblock.events.SkyblockAbilityUseEvent;
import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.item.ability.AbilityType;
import com.github.unjoinable.skyblock.item.component.components.AbilityComponent;
import com.github.unjoinable.skyblock.registry.registries.AbilityRegistry;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerUseItemListener implements EventListener<PlayerUseItemEvent> {
    @Override
    public @NotNull Class<PlayerUseItemEvent> eventType() {
        return PlayerUseItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerUseItemEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

        if (event.getHand() == Player.Hand.OFF) return Result.INVALID;

        ItemStack item = event.getItemStack();

        if (item.hasTag(AbilityComponent.ABILITY_TAG)) {
            String tag = item.getTag(AbilityComponent.ABILITY_TAG);
            AbilityRegistry registry = AbilityRegistry.getInstance();
            Ability ability = registry.getRegisteredAbility(tag);

            if (ability.type() == AbilityType.RIGHT_CLICK) {
                SkyblockAbilityUseEvent abilityUseEvent = new SkyblockAbilityUseEvent(
                        player,
                        SkyblockItem.fromItemStack(item),
                        ability);
                MinecraftServer.getGlobalEventHandler().call(abilityUseEvent);
            }
        }
        return Result.SUCCESS;
    }
}
