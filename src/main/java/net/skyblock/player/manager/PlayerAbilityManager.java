package net.skyblock.player.manager;

import net.skyblock.item.ability.Ability;
import net.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages ability cooldowns for a player, providing thread-safe and efficient
 * tracking of ability usage and remaining cooldown times.
 * <p>
 * Key Features:
 * <ul>
 *   <li>Concurrent and thread-safe ability cooldown tracking</li>
 *   <li>Efficient cooldown calculation</li>
 *   <li>Precise millisecond-level cooldown management</li>
 * </ul>
 */
public class PlayerAbilityManager {
    private final SkyblockPlayer player;
    private final Map<Ability, Long> abilityLastUses = new ConcurrentHashMap<>();

    /**
     * Constructs a new PlayerAbilityManager for a specific Skyblock player.
     * <p>
     * This manager tracks and manages ability cooldowns for an individual player.
     * Each ability manager is tightly coupled with a specific player instance.
     * </p>
     *
     * @param player The SkyblockPlayer associated with this ability manager
     */
    public PlayerAbilityManager(@NotNull SkyblockPlayer player) {
        this.player = player;
    }

    /**
     * Determines if an ability can be used based on its cooldown.
     *
     * @param ability The ability to check
     * @return true if the ability is off cooldown, false otherwise
     */
    public boolean canUseAbility(@NotNull Ability ability) {
        return abilityLastUses.compute(ability, (_, lastUse) -> {
            long now = System.currentTimeMillis();

            // If no previous use or cooldown has expired
            if (lastUse == null || now - lastUse > ticksToMilliseconds(ability.cooldown())) {
                return null; // Remove from map, ability is available
            }

            return lastUse; // Keep existing cooldown
        }) == null;
    }

    /**
     * Calculates the remaining cooldown time for a specific ability.
     *
     * @param ability The ability to check
     * @return Remaining cooldown time in milliseconds, or 0 if ability is ready
     */
    public long getRemainingCooldown(@NotNull Ability ability) {
        Long lastUse = abilityLastUses.get(ability);
        if (lastUse == null) {
            return 0;
        }

        long now = System.currentTimeMillis();
        long cooldownMs = ticksToMilliseconds(ability.cooldown());
        long remainingCooldown = cooldownMs - (now - lastUse);

        return Math.max(0, remainingCooldown);
    }

    /**
     * Starts the cooldown for a specific ability.
     *
     * @param ability The ability to put on cooldown
     */
    public void startCooldown(@NotNull Ability ability) {
        abilityLastUses.put(ability, System.currentTimeMillis());
    }

    /**
     * Converts Minecraft ticks to milliseconds.
     *
     * @param ticks Number of Minecraft ticks
     * @return Equivalent time in milliseconds
     */
    private static long ticksToMilliseconds(long ticks) {
        return ticks * 50L;
    }

    /**
     * Clears the cooldown for a specific ability.
     *
     * @param ability The ability to reset
     */
    public void resetCooldown(@NotNull Ability ability) {
        abilityLastUses.remove(ability);
    }

    /**
     * Checks if an ability is currently on cooldown.
     *
     * @param ability The ability to check
     * @return true if the ability is on cooldown, false otherwise
     */
    public boolean isOnCooldown(@NotNull Ability ability) {
        return !canUseAbility(ability);
    }
}