package net.unjoinable.skyblock.player.systems;

import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.ability.AbilityCostType;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.item.ability.traits.ShortbowAbility;
import net.unjoinable.skyblock.item.attribute.impls.AbilityAttribute;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.player.PlayerSystem;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.ui.actionbar.ActionBarDisplay;
import net.unjoinable.skyblock.ui.actionbar.ActionBarPurpose;
import net.unjoinable.skyblock.ui.actionbar.ActionBarSection;

import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.unjoinable.skyblock.combat.statistic.Statistic.BONUS_ATTACK_SPEED;

/**
 * Manages ability cooldowns and execution for players.
 */
public class AbilitySystem implements PlayerSystem {
    private static final ActionBarDisplay NOT_ENOUGH_MANA = new ActionBarDisplay(
            text("NOT ENOUGH MANA", RED, BOLD),
            40,
            90,
            ActionBarPurpose.ABILITY
    );

    private final SkyblockPlayer player;
    private final ItemProcessor itemProcessor;
    private final Map<Key, Long> cooldowns = new HashMap<>();
    private boolean initialized;

    public AbilitySystem(SkyblockPlayer player, ItemProcessor itemProcessor) {
        this.player = player;
        this.itemProcessor = itemProcessor;
    }

    /**
     * Initializes the ability system.
     */
    @Override
    public void start() {
        initialized = true;
    }

    /**
     * Checks if the ability system has been initialized.
     */
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    // Main ability execution methods

    /**
     * Attempts to use an ability if it's ready and player has sufficient resources.
     *
     * @param ability the ability to use
     * @param item    the item containing the ability
     */
    public void tryUse(ItemAbility ability, SkyblockItem item) {
        if (!isReady(ability)) {
            return;
        }

        if (!hasResources(ability)) {
            sendInsufficientResourceMessage(ability.costType());
            return;
        }

        execute(ability, item);
    }

    /**
     * Attempts to trigger an ability from an item stack based on execution type.
     *
     * @param itemStack the item stack to check for abilities
     * @param trigger the execution trigger (e.g., RIGHT_CLICK, LEFT_CLICK)
     */
    public void tryUse(ItemStack itemStack, ExecutionType trigger) {
        SkyblockItem item = itemProcessor.fromItemStack(itemStack);
        item.attributes().get(AbilityAttribute.class).ifPresent(attribute -> {
            for (ItemAbility ability : attribute.abilities()) {
                if (ability.trigger() == trigger) {
                    tryUse(ability, item);
                    return;
                }
            }
        });
    }

    // Ability state checks
    /**
     * Checks if an ability is off cooldown and ready to use.
     *
     * @param ability the ability to check
     * @return true if ability is ready, false if still on cooldown
     */
    public boolean isReady(ItemAbility ability) {
        Long lastUsed = cooldowns.get(ability.key());
        if (lastUsed == null) return true;

        long cooldown = ability.cooldown();

        if (ability instanceof ShortbowAbility) {
            double atkSpeed = player.getStatSystem().getStat(BONUS_ATTACK_SPEED);
            cooldown = calcShortBowCooldown((int) atkSpeed);
        }

        return System.currentTimeMillis() - lastUsed >= cooldown;
    }

    /**
     * Checks if the player has sufficient resources to use an ability.
     *
     * @param ability the ability to check cost for
     * @return true if player can afford the ability, false otherwise
     */
    public boolean hasResources(ItemAbility ability) {
        return switch (ability.costType()) {
            case MANA -> player.getStatSystem().getCurrentMana() >= ability.cost();
            case COINS -> player.getEconomySystem().hasCoins(ability.cost());
            case HEALTH -> player.getStatSystem().getCurrentHealth() > ability.cost();
            case FREE -> true;
        };
    }

    /**
     * Gets the remaining cooldown time for an ability.
     *
     * @param ability the ability to check
     * @return remaining cooldown in milliseconds, or 0 if ready
     */
    public long getRemainingCooldown(ItemAbility ability) {
        Long lastUsed = cooldowns.get(ability.key());
        if (lastUsed == null) return 0;

        long elapsed = System.currentTimeMillis() - lastUsed;
        return Math.max(0, ability.cooldown() - elapsed);
    }

    // Cooldown management
    /**
     * Clears the cooldown for a specific ability, making it immediately usable.
     *
     * @param ability the ability to reset
     */
    public void clearCooldown(ItemAbility ability) {
        cooldowns.remove(ability.key());
    }

    /**
     * Clears all ability cooldowns for this player.
     */
    public void clearAllCooldowns() {
        cooldowns.clear();
    }

    // Private execution logic
    /**
     * Executes an ability: starts cooldown, consumes resources, runs action, and shows feedback.
     */
    private void execute(ItemAbility ability, SkyblockItem item) {
        cooldowns.put(ability.key(), System.currentTimeMillis());
        consumeResources(ability);
        ability.action().accept(player, item);
        showFeedback(ability);
    }

    /**
     * Consumes the required resources for an ability based on its cost type.
     */
    private void consumeResources(ItemAbility ability) {
        switch (ability.costType()) {
            case MANA -> player.getStatSystem().consumeMana(ability.cost());
            case COINS -> player.getEconomySystem().removeCoins(ability.cost());
            case HEALTH -> player.getStatSystem().consumeHealth(ability.cost());
            case FREE -> {}
        }
    }

    /**
     * Sends appropriate message when player lacks resources for an ability.
     */
    private void sendInsufficientResourceMessage(AbilityCostType costType) {
        if (costType == AbilityCostType.MANA) {
            player.getActionBar().addReplacement(ActionBarSection.MANA, NOT_ENOUGH_MANA);
        }
    }

    /**
     * Shows visual feedback for ability usage.
     */
    private void showFeedback(ItemAbility ability) {
        if (ability.costType() == AbilityCostType.MANA) {
            var display = new ActionBarDisplay(
                    text("-" + ability.cost() + " Mana (", AQUA)
                            .decoration(ITALIC, false)
                            .append(text(ability.displayName(), GOLD))
                            .append(text(")", AQUA)),
                    40, 100, ActionBarPurpose.ABILITY
            );
            player.getActionBar().addReplacement(ActionBarSection.DEFENSE, display);
        }
    }

    /**
     * Calculates the cooldown time for a short bow based on attack speed.
     * - Cooldown is 0.5s when attack speed <= 0
     * - Cooldown is 0.25s when attack speed >= 100
     * - Cooldown decreases linearly between 0 and 100
     *
     * @param attackSpeed The attack speed value (can be below 0 or above 100)
     * @return The cooldown time in seconds.
     */
    private static long calcShortBowCooldown(int attackSpeed) {
        long startCooldown = 500;
        long endCooldown = 250;

        if (attackSpeed <= 0) {
            return startCooldown;
        } else if (attackSpeed >= 100) {
            return endCooldown;
        }

        return (long) (startCooldown - (attackSpeed / 100.0) * (startCooldown - endCooldown));
    }
}