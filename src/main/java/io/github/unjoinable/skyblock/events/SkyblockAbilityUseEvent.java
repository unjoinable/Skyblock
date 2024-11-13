package io.github.unjoinable.skyblock.events;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

public class SkyblockAbilityUseEvent implements Event, CancellableEvent, PlayerInstanceEvent {
    private boolean isCancelled = false;

    private final SkyblockPlayer player;
    private final Ability ability;
    private final SkyblockItem item;

    public SkyblockAbilityUseEvent(@NotNull SkyblockPlayer player, SkyblockItem item, @NotNull Ability ability) {
        this.player = player;
        this.ability = ability;
        this.item = item;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public Ability getAbility() {
        return ability;
    }

    public SkyblockItem getItem() {
        return item;
    }
}
