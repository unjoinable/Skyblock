package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.item.ability.AbilityCostType;
import io.github.unjoinable.skyblock.skill.Skill;
import io.github.unjoinable.skyblock.statistics.Statistic;
import io.github.unjoinable.skyblock.user.actionbar.ActionBar;
import io.github.unjoinable.skyblock.user.actionbar.ActionBarDisplayReplacement;
import io.github.unjoinable.skyblock.user.actionbar.ActionBarPurpose;
import io.github.unjoinable.skyblock.user.actionbar.ActionBarSection;
import io.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * Represents a player in the Skyblock game, extending the base Player class.
 * This class manages player-specific data such as coins, bits, skills, abilities etc.
 * It also handles the player's action bar and statistics.
 */
public class SkyblockPlayer extends Player {
    private final ActionBar actionBar;
    private final StatisticsHandler statsHandler;
    private final AbilityHandler abilityHandler;

    // Static components for action bar messages
    private static final Component NOT_ENOUGH_MANA = Component.text("NOT ENOUGH MANA", RED, BOLD).decoration(ITALIC, false);
    private static final ActionBarDisplayReplacement NOT_ENOUGH_MANA_REPLACEMENT = new ActionBarDisplayReplacement(
            NOT_ENOUGH_MANA,
            40,
            10,
            ActionBarPurpose.ABILITY
    );

    // Player data
    private long coins = 0;
    private long bits = 0;
    private Map<Skill, Long> skills = new EnumMap<>(Skill.class);

    public SkyblockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        actionBar = new ActionBar();
        statsHandler = new StatisticsHandler(this);
        abilityHandler = new AbilityHandler(this);
    }

    /**
     * Updates the player's item cache.
     * This method clears the existing cache and repopulates it with the current items in the player's inventory.
     * Items with the ID "AIR" are ignored and not added to the cache.
     */
    public void updateItemCache() {
        Map<ItemSlot, SkyblockItem> oldCache = PlayerItemCache.fromCache(this).getAll();
        oldCache.clear();
        for (ItemSlot value : ItemSlot.getValues()) {
            SkyblockItem item = SkyblockItem.fromItemStack(value.get(this));
            if (item.id().equals(NamespacedId.AIR)) continue;
            oldCache.put(value, item);
        }
    }

    /**
     * Determines if the player can use a specified ability based on its cost type and cost.
     *
     * @param ability the ability to check, which contains the cost type and cost.
     * @return true if the player has enough resources (mana, health, or coins) to use the ability; false otherwise.
     */
    public boolean canUseAbility(@NotNull Ability ability) {
        AbilityCostType costType = ability.costType();
        int abilityCost = ability.abilityCost();

        return switch (costType) {
            case MANA -> statsHandler.getMana() > abilityCost;
            case HEALTH -> statsHandler.getHealth() > abilityCost;
            case COINS -> coins > abilityCost;
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
            case MANA -> actionBar.addReplacement(ActionBarSection.MANA, NOT_ENOUGH_MANA_REPLACEMENT);
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
        int abilityCost = ability.abilityCost();

        switch (costType) {
            case MANA -> {
                statsHandler.setMana(statsHandler.getMana() - abilityCost);
                ActionBarDisplayReplacement replacement = abilityUseReplacement(ability, abilityCost);
                actionBar.addReplacement(ActionBarSection.DEFENSE, replacement);
            }
            case HEALTH -> statsHandler.setHealth(statsHandler.getHealth() - abilityCost);
            case COINS -> coins -= abilityCost;
        }
    }

    /**
     * Executes the player's task loop, updating the item cache and statistics.
     * Updates the action bar with the player's current health, defense, and mana.
     */
    public void taskLoop() {
        updateItemCache();
        statsHandler.taskLoop();

        DecimalFormat df = new DecimalFormat("#");
        actionBar.setDefaultDisplay(ActionBarSection.HEALTH, Component.text(df.format(statsHandler.getHealth()) + "/" + df.format(statsHandler.getStat(Statistic.HEALTH)) + "❤", RED));
        actionBar.setDefaultDisplay(ActionBarSection.DEFENSE, Component.text(df.format(statsHandler.getStat(Statistic.DEFENSE)) + "❈ Defense", GREEN));
        actionBar.setDefaultDisplay(ActionBarSection.MANA, Component.text(df.format(statsHandler.getMana()) + "/" + df.format(statsHandler.getStat(Statistic.INTELLIGENCE) )+ "✎ Mana", AQUA));
        sendActionBar(actionBar.build());
    }

    /**
     * Creates an action bar display replacement for when an ability is used.
     *
     * @param ability the ability being used.
     * @param abilityCost the cost of the ability.
     * @return an ActionBarDisplayReplacement reflecting the ability usage.
     */
    private ActionBarDisplayReplacement abilityUseReplacement(Ability ability, int abilityCost) {
        return new ActionBarDisplayReplacement(
                Component.text("-" + abilityCost + " Mana (", AQUA)
                        .append(Component.text(ability.name(), GOLD))
                        .append(Component.text(")", AQUA).decoration(ITALIC, false)),
                100,
                5,
                ActionBarPurpose.ABILITY);
    }

    // Getters

    /**
     * Gets the number of bits the player has.
     *
     * @return the number of bits.
     */
    public long getBits() {
        return bits;
    }

    /**
     * Gets the number of coins the player has.
     *
     * @return the number of coins.
     */
    public long getCoins() {
        return coins;
    }

    /**
     * Gets all the skills and their corresponding experience points for the player.
     *
     * @return a map of skills to experience points.
     */
    public @NotNull Map<Skill, Long> getAllSkillsXP() {
        return skills;
    }

    /**
     * Gets the experience points for a specific skill.
     *
     * @param skill the skill to retrieve experience points for.
     * @return the experience points for the specified skill.
     */
    public long getSkillXP(@NotNull Skill skill) {
        return skills.getOrDefault(skill, 0L);
    }

    /**
     * Gets the player's action bar.
     *
     * @return the action bar.
     */
    public ActionBar getActionBar() {
        return actionBar;
    }

    /**
     * Gets the player's statistics handler.
     *
     * @return the statistics handler.
     */
    public StatisticsHandler getStatsHandler() {
        return statsHandler;
    }

    /**
     * Gets the player's ability handler.
     *
     * @return the ability handler.
     */
    public AbilityHandler getAbilityHandler() {
        return abilityHandler;
    }

    // Setters

    /**
     * Sets the number of bits the player has.
     *
     * @param bits the number of bits to set.
     */
    public void setBits(long bits) {
        this.bits = bits;
    }

    /**
     * Sets the number of coins the player has.
     *
     * @param coins the number of coins to set.
     */
    public void setCoins(long coins) {
        this.coins = coins;
    }

    /**
     * Sets the experience points for a specific skill.
     *
     * @param skill the skill to set experience points for.
     * @param value the experience points to set.
     */
    public void setSkillXP(@NotNull Skill skill, long value) {
        this.skills.put(skill, value);
    }
}
