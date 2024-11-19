package io.github.unjoinable.skyblock.events;

import io.github.unjoinable.skyblock.statistics.CombatEntity;
import io.github.unjoinable.skyblock.statistics.SkyblockDamage;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.jetbrains.annotations.NotNull;

public class SkyblockDamageEvent implements Event, CancellableEvent {
    private final SkyblockDamage damage;
    private boolean isCancelled = false;

    public SkyblockDamageEvent(@NotNull SkyblockDamage damage) {
        this.damage = damage;
    }

    /**
     * Gets the amount of damage dealt.
     *
     * @return The damage value.
     */
    public double getDamage() {
        return damage.damage();
    }

    public @NotNull SkyblockDamage getSkyblockDamage() {
        return damage;
    }

    /**
     * Checks if the damage is critical.
     *
     * @return True if the damage is critical, false otherwise.
     */
    public boolean isCriticalDamage() {
        return damage.isCriticalDamage();
    }

    /**
     * Gets the target entity receiving the damage.
     *
     * @return The target entity.
     */
    public CombatEntity getTarget() {
        return damage.target();
    }
    
    /**
     * Gets the player dealing the damage.
     *
     * @return The player.
     */
    public @NotNull CombatEntity getSource() {
        return damage.source();
    }

    /**
     * Checks if the damage is magical.
     *
     * @return True if the damage is magical, false otherwise.
     */
    public boolean isMagicDamage() {
        return isMagicDamage();
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
