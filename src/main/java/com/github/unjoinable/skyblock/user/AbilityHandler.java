package com.github.unjoinable.skyblock.user;

import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.item.ability.AbilityCostType;
import com.github.unjoinable.skyblock.user.actionbar.ActionBarDisplayReplacement;
import com.github.unjoinable.skyblock.user.actionbar.ActionBarPurpose;
import com.github.unjoinable.skyblock.user.actionbar.ActionBarSection;
import com.github.unjoinable.skyblock.util.MiniString;
import com.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the abilities of a SkyblockPlayer, managing cooldowns and usage.
 * This class tracks when abilities are used and ensures that they respect their cooldown periods.
 */
public class AbilityHandler {
    // Static components for action bar messages
    private static final Component NOT_ENOUGH_MANA = MiniString.toComponent("<red><bold>NOT ENOUGH MANA");
    private static final ActionBarDisplayReplacement NOT_ENOUGH_MANA_REPLACEMENT = new ActionBarDisplayReplacement(NOT_ENOUGH_MANA, 40, 10, ActionBarPurpose.ABILITY);

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

    /**
     * Determines if the player can use a specified ability based on its cost type and cost.
     *
     * @param ability the ability to check, which contains the cost type and cost.
     * @return true if the player has enough resources (mana, health, or coins) to use the ability; false otherwise.
     */
    public boolean canAffordAbilityCost(@NotNull Ability ability) {
        AbilityCostType costType = ability.costType();
        int abilityCost = ability.abilityCost(player);

        return switch (costType) {
            case MANA -> player.getStatsHandler().getMana() > abilityCost;
            case HEALTH -> player.getStatsHandler().getCurrentHealth() > abilityCost;
            case COINS -> player.getCoins() > abilityCost;
            case NONE -> true;
        };
    }

    /**
     * Handles the scenario where an ability cannot be used due to insufficient resources.
     * Displays an appropriate message on the action bar.
     *
     * @param ability the ability that failed to be used.
     */
    public void abilityFailed(@NotNull Ability ability) {
        AbilityCostType costType = ability.costType();

        switch (costType) {
            case MANA -> player.getActionBar().addReplacement(ActionBarSection.MANA, NOT_ENOUGH_MANA_REPLACEMENT);
            default -> { return; } //TODO
        }
    }

    /**
     * Uses a specified ability, deducting the appropriate resource cost from the player.
     * Updates the action bar to reflect the ability usage.
     *
     * @param ability the ability to use.
     */
    public void useAbility(@NotNull Ability ability) {
        AbilityCostType costType = ability.costType();
        int abilityCost = ability.abilityCost(player);
        StatisticsHandler statsHandler = player.getStatsHandler();

        switch (costType) {
            case MANA -> {
                statsHandler.setMana(statsHandler.getMana() - abilityCost);
                ActionBarDisplayReplacement replacement = abilityUseReplacement(ability, abilityCost);
                player.getActionBar().addReplacement(ActionBarSection.DEFENSE, replacement);
            }
            case HEALTH -> statsHandler.subtractCurrentHealth(abilityCost);
            case COINS ->  {
                long coins = player.getCoins();
                player.setCoins(coins - abilityCost);
            }
        }
    }

    /**
     * Creates an action bar display replacement for when an ability is used.
     *
     * @param ability the ability being used.
     * @param abilityCost the cost of the ability.
     * @return an ActionBarDisplayReplacement reflecting the ability usage.
     */
    private ActionBarDisplayReplacement abilityUseReplacement(Ability ability, int abilityCost) {
        Component text =  MiniString.toComponent("<aqua>-<ability_cost> Mana (<gold><ability_name></gold>)",
                Placeholder.parsed("ability_cost", String.valueOf(abilityCost)),
                Placeholder.parsed("ability_name", ability.name()));
        return new ActionBarDisplayReplacement(text, 100, 5, ActionBarPurpose.ABILITY);
    }
}
