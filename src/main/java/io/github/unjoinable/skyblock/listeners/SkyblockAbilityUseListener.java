package io.github.unjoinable.skyblock.listeners;

import io.github.unjoinable.skyblock.events.SkyblockAbilityUseEvent;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.user.AbilityHandler;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.user.StatisticsHandler;
import net.minestom.server.event.EventListener;
import org.jetbrains.annotations.NotNull;

public class SkyblockAbilityUseListener implements EventListener<SkyblockAbilityUseEvent> {

    @Override
    public @NotNull Class<SkyblockAbilityUseEvent> eventType() {
        return SkyblockAbilityUseEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull SkyblockAbilityUseEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        AbilityHandler abilityHandler = player.getAbilityHandler();
        StatisticsHandler statsHandler = player.getStatsHandler();
        Ability ability = event.getAbility();

        if (!abilityHandler.canUseAbility(ability) && !player.canUseAbility(ability)) {
            event.setCancelled(true);
            player.abilityFailed(ability);
            return Result.INVALID;
        }

        player.useAbility(ability); //taxes for using ability
        ability.run(player, event.getItem());
        return Result.SUCCESS;
    }
}
