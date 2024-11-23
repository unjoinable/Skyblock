package com.github.unjoinable.skyblock.events;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event that is triggered when a Skyblock ability is used.
 * This does not guarantee that the ability is successfully ran.
 */
public class SkyblockAbilityUseEvent implements Event, CancellableEvent, PlayerInstanceEvent {
    private boolean isCancelled = false;

    private final SkyblockPlayer player;
    private final Ability ability;
    private final SkyblockItem item;

    /**
     * Constructs a new SkyblockAbilityUseEvent.
     *
     * @param player  The SkyblockPlayer who is using the ability.
     * @param item    The SkyblockItem associated with the ability being used.
     * @param ability The Ability being used.
     */
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

    /**
     * Gets the ability being used in this event.
     *
     * @return The Ability being used.
     */
    public Ability getAbility() {
        return ability;
    }

    /**
     * Gets the item associated with the ability being used.
     *
     * @return The SkyblockItem associated with the ability.
     */
    public SkyblockItem getItem() {
        return item;
    }
}
