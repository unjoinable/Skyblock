package net.unjoinable.skyblock.event.custom;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.player.SkyblockPlayer;

/**
 * Represents an event that is triggered when a player uses an item ability.
 */
public class PlayerUseAbilityEvent implements PlayerEvent, CancellableEvent {
    private final SkyblockPlayer player;
    private final ItemAbility itemAbility;
    private boolean cancelled;

    /**
     * Constructs a new PlayerUseAbilityEvent.
     *
     * @param player The Skyblock player who is using the ability
     * @param itemAbility The item ability being used
     */
    public PlayerUseAbilityEvent(SkyblockPlayer player, ItemAbility itemAbility) {
        this.player = player;
        this.itemAbility = itemAbility;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the item ability associated with this event.
     *
     * @return The item ability being used by the player
     */
    public ItemAbility getItemAbility() {
        return itemAbility;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
