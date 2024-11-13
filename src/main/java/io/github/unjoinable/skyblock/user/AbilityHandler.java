package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.util.NamespacedId;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the abilities of a SkyblockPlayer, managing cooldowns and usage.
 * This class tracks when abilities are used and ensures that they respect their cooldown periods.
 */
public class AbilityHandler {
    private final SkyblockPlayer player;
    private final Map<NamespacedId, Long> cooldowns;

    /**
     * Constructs an AbilityHandler for the specified player.
     *
     * @param player the player whose abilities are managed by this handler.
     */
    public AbilityHandler(@NotNull SkyblockPlayer player) {
        this.player = player;
        this.cooldowns = new HashMap<>();
    }

    /**
     * Checks if the specified ability can be used by the player.
     *
     * @param ability the ability to check for availability.
     * @return {@code true} if the ability can be used (i.e., it is not on cooldown),
     *         {@code false} otherwise.
     */
    public boolean canUseAbility(@NotNull Ability ability) {
        NamespacedId id = ability.id();
        return !cooldowns.containsKey(id) || getRemainingCooldown(ability) == 0;
    }

    /**
     * Gets the remaining cooldown time for the specified ability.
     *
     * @param ability the ability to check for remaining cooldown.
     * @return the remaining cooldown time in milliseconds. Returns 0 if the ability is ready to use.
     */
    public long getRemainingCooldown(@NotNull Ability ability) {
        long lastUse = cooldowns.get(ability.id());
        long cooldown = ability.cooldownInMs();
        long current = System.currentTimeMillis();

        long remaining = (lastUse + cooldown) - current;

        return remaining < 0 ? 0 : remaining;
    }

    /**
     * Starts the cooldown for the specified ability.
     * If the ability has no cooldown, this method does nothing.
     *
     * @param ability the ability to start the cooldown for.
     */
    public void startCooldown(@NotNull Ability ability) {
        if (ability.cooldownInMs() == 0) return;
        cooldowns.put(ability.id(), System.currentTimeMillis());
    }
}
