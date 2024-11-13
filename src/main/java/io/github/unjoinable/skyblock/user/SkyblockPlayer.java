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

public class SkyblockPlayer extends Player {
    private final ActionBar actionBar;
    private final StatisticsHandler statsHandler;
    private final AbilityHandler abilityHandler;

    //statics
    private static final Component NOT_ENOUGH_MANA = Component.text("NOT ENOUGH MANA", RED, BOLD).decoration(ITALIC, false);
    private static final ActionBarDisplayReplacement NOT_ENOUGH_MANA_REPLACEMENT = new ActionBarDisplayReplacement(
            NOT_ENOUGH_MANA,
            40,
            10,
            ActionBarPurpose.ABILITY
    );

    //player data
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

    public boolean canUseAbility(@NotNull Ability ability) {
        AbilityCostType costType = ability.costType();
        int abilityCost = ability.abilityCost();

        return switch (costType) {
            case MANA -> statsHandler.getMana() > abilityCost;
            case HEALTH -> statsHandler.getHealth() > abilityCost;
            case COINS -> coins > abilityCost;
        };
    }

    public void abilityFailed(@NotNull Ability ability) {
        AbilityCostType costType = ability.costType();

        switch (costType) {
            case MANA -> actionBar.addReplacement(ActionBarSection.MANA, NOT_ENOUGH_MANA_REPLACEMENT);
        }
    }

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

    public void taskLoop() {
        updateItemCache();
        statsHandler.taskLoop();

        DecimalFormat df = new DecimalFormat("#");
        actionBar.setDefaultDisplay(ActionBarSection.HEALTH, Component.text(df.format(statsHandler.getHealth()) + "/" + df.format(statsHandler.getStat(Statistic.HEALTH)) + "❤", RED));
        actionBar.setDefaultDisplay(ActionBarSection.DEFENSE, Component.text(df.format(statsHandler.getStat(Statistic.DEFENSE)) + "❈ Defense", GREEN));
        actionBar.setDefaultDisplay(ActionBarSection.MANA, Component.text(df.format(statsHandler.getMana()) + "/" + df.format(statsHandler.getStat(Statistic.INTELLIGENCE) )+ "✎ Mana", AQUA));
        sendActionBar(actionBar.build());
    }

    private ActionBarDisplayReplacement abilityUseReplacement(Ability ability, int abilityCost) {
        return new ActionBarDisplayReplacement(
                Component.text("-" + abilityCost + " Mana (", AQUA)
                        .append(Component.text(ability.name(), GOLD))
                        .append(Component.text(")", AQUA).decoration(ITALIC, false)),
                100,
                5,
                ActionBarPurpose.ABILITY);
    }
    //override methods

    @Override
    public void closeInventory() {
        super.closeInventory();
    }


    //getters

    public long getBits() {
        return bits;
    }

    public long getCoins() {
        return coins;
    }

    public @NotNull Map<Skill, Long> getAllSkillsXP() {
        return skills;
    }

    public long getSkillXP(@NotNull Skill skill) {
        return skills.getOrDefault(skill, 0L);
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public StatisticsHandler getStatsHandler() {
        return statsHandler;
    }

    public AbilityHandler getAbilityHandler() {
        return abilityHandler;
    }

    //setters

    public void setBits(long bits) {
        this.bits = bits;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public void setSkillXP(@NotNull Skill skill, long value) {
        this.skills.put(skill, value);
    }

}
