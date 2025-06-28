package net.unjoinable.skyblock.player.systems;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.ability.AbilityCostType;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.item.attribute.impls.AbilityAttribute;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.player.PlayerSystem;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.ui.actionbar.ActionBarDisplay;
import net.unjoinable.skyblock.ui.actionbar.ActionBarPurpose;
import net.unjoinable.skyblock.ui.actionbar.ActionBarSection;
import net.unjoinable.skyblock.utils.NamespaceId;

import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

/**
 * Manages ability cooldowns and execution for players.
 */
public class AbilitySystem implements PlayerSystem {
    private static final Component NOT_ENOUGH_MANA = text("NOT ENOUGH MANA", RED, BOLD);
    private static final Component NOT_ENOUGH_COINS = text("NOT ENOUGH COINS", RED, BOLD);
    private static final Component NOT_ENOUGH_HEALTH = text("NOT ENOUGH HEALTH", RED, BOLD);

    private final SkyblockPlayer player;
    private final ItemProcessor itemProcessor;
    private final Map<NamespaceId, Long> cooldowns = new HashMap<>();
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
        Long lastUsed = cooldowns.get(ability.id());
        if (lastUsed == null) return true;

        return System.currentTimeMillis() - lastUsed >= ability.cooldown();
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
        Long lastUsed = cooldowns.get(ability.id());
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
        cooldowns.remove(ability.id());
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
        cooldowns.put(ability.id(), System.currentTimeMillis());
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
        switch (costType) {
            case MANA -> player.sendMessage(NOT_ENOUGH_MANA);
            case COINS -> player.sendMessage(NOT_ENOUGH_COINS);
            case HEALTH -> player.sendMessage(NOT_ENOUGH_HEALTH);
            case FREE -> {} // No message needed for free abilities
        }
    }

    /**
     * Shows visual feedback for ability usage.
     */
    private void showFeedback(ItemAbility ability) {
        switch (ability.costType()) {
            case MANA -> {
                var display = new ActionBarDisplay(
                        text(ability.id().toString()),
                        40, 100,
                        ActionBarPurpose.ABILITY
                );
                player.getActionBar().addReplacement(ActionBarSection.DEFENSE, display);
            }
            default -> {}
        }
    }
}