package com.github.unjoinable.skyblock.listeners;

import com.github.unjoinable.skyblock.events.SkyblockAbilityUseEvent;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.user.AbilityHandler;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.user.StatisticsHandler;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventListener;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class SkyblockAbilityUseListener implements EventListener<SkyblockAbilityUseEvent> {
    private static final Component COOLDOWN = Component.text("The ability is currently on cooldown", RED).decoration(ITALIC, false);

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

        if (!abilityHandler.canUseAbility(ability)) {
            player.sendMessage(COOLDOWN);
            event.setCancelled(true);
            return Result.INVALID;
        }

        if (!player.canUseAbility(ability)) {
            event.setCancelled(true);
            player.abilityFailed(ability);
            return Result.INVALID;
        }

        player.useAbility(ability); //taxes for using ability
        abilityHandler.startCooldown(ability);
        ability.run(player, event.getItem());
        return Result.SUCCESS;
    }
}
