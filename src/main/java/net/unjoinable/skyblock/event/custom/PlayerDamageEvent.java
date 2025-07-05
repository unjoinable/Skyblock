package net.unjoinable.skyblock.event.custom;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.entity.SkyblockEntity;
import net.unjoinable.skyblock.player.SkyblockPlayer;

/**
 * Event fired when a player deals damage to an entity.
 */
public class PlayerDamageEvent implements PlayerEvent, CancellableEvent {
    private final SkyblockPlayer player;
    private final SkyblockEntity target;
    private final SkyblockDamage damage;
    private boolean cancelled;

    /**
     * Creates a new player damage event.
     */
    public PlayerDamageEvent(SkyblockPlayer player, SkyblockEntity target, SkyblockDamage damage) {
        this.player = player;
        this.target = target;
        this.damage = damage;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the damage dealt
     */
    public SkyblockDamage getDamage() {
        return damage;
    }

    /**
     * @return the entity that received damage
     */
    public SkyblockEntity getTarget() {
        return target;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = true;
    }
}
