package io.github.unjoinable.skyblock.user;

import io.github.unjoinable.skyblock.entity.SkyblockEntity;
import io.github.unjoinable.skyblock.events.SkyblockDamageEvent;
import io.github.unjoinable.skyblock.island.Island;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.item.ability.AbilityCostType;
import io.github.unjoinable.skyblock.skill.Skill;
import io.github.unjoinable.skyblock.statistics.*;
import io.github.unjoinable.skyblock.user.actionbar.ActionBar;
import io.github.unjoinable.skyblock.user.actionbar.ActionBarDisplayReplacement;
import io.github.unjoinable.skyblock.user.actionbar.ActionBarPurpose;
import io.github.unjoinable.skyblock.user.actionbar.ActionBarSection;
import io.github.unjoinable.skyblock.util.MiniString;
import io.github.unjoinable.skyblock.util.NamespacedId;
import io.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

/**
 * Represents a player in the Skyblock game, extending the base Player class.
 * This class manages player-specific data such as coins, bits, skills, abilities etc.
 * It also handles the player's action bar and statistics.
 */
public class SkyblockPlayer extends Player implements CombatEntity {
    private final ActionBar actionBar;
    private final StatisticsHandler statsHandler;
    private final AbilityHandler abilityHandler;

    private boolean isInvulnerable;

    // Static components for action bar messages
    private static final Component NOT_ENOUGH_MANA = Component.text("NOT ENOUGH MANA", RED, BOLD).decoration(ITALIC, false);
    private static final ActionBarDisplayReplacement NOT_ENOUGH_MANA_REPLACEMENT = new ActionBarDisplayReplacement(
            NOT_ENOUGH_MANA,
            40,
            10,
            ActionBarPurpose.ABILITY
    );

    // Player data
    private Island island = Island.HUB;
    private int skyblockXp;
    private int skyblockLvl;
    private long coins = 0;
    private long bits = 0;
    private final Map<Skill, Long> skills = new EnumMap<>(Skill.class);

    public SkyblockPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        PlayerItemCache.addToCache(this);
        getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40D);
        statsHandler = new StatisticsHandler(this);
        abilityHandler = new AbilityHandler(this);
        actionBar = new ActionBar();
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

    @Override
    public void setHealth(float health) {
        if (health <= 0) {
            kill();
            return;
        }

        double maxHealth = statsHandler.getStat(Statistic.HEALTH);
        sendPacket(new UpdateHealthPacket((float) ((health / maxHealth) * 40), 20, 20));
    }


    @Override
    public void heal() {
        statsHandler.healHealth();
        statsHandler.healMana();
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
            case HEALTH -> getHealth() > abilityCost;
            case COINS -> coins > abilityCost;
        };
    }

    @Override
    public @NotNull Entity getEntity() {
        return this;
    }

    @Override
    public double getCurrentHealth() {
        return statsHandler.getCurrentHealth();
    }

    @Override
    public void setCurrentHealth(double health) {
        if (health < 0) kill();
        statsHandler.setCurrentHealth(health);
    }

    @Override
    public double getMaxHealth() {
        return statsHandler.getStat(Statistic.HEALTH);
    }

    @Override
    public boolean isInvunerable() {
        return isInvulnerable;
    }

    @Override
    public void setInvunerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
    }

    @Override
    public void meleeDamage(@NotNull CombatEntity target) {
        double baseDamage = statsHandler.getStat(Statistic.DAMAGE);
        double strength = statsHandler.getStat(Statistic.STRENGTH);
        double critDamage = statsHandler.getStat(Statistic.CRIT_DAMAGE);
        double critChance = statsHandler.getStat(Statistic.CRIT_CHANCE);

        double damage = (5 + baseDamage) * (1 + strength/100);

        boolean isCriticalHit = critChance >= 100 || Utils.probabilityCheck((int) critChance);

        if (isCriticalHit) {
            damage *= critDamage;
        }

        EventDispatcher.call(new SkyblockDamageEvent(
                new SkyblockDamage(
                        false,
                        damage,
                        isCriticalHit,
                        DamageReason.MELEE,
                        this,
                        target
                )
        ));
        double ferocity = statsHandler.getStat(Statistic.FEROCITY);
        if (ferocity == 0) return;
        int conditionalLoop = Utils.probabilityCheck((int) (ferocity % 100)) ? 1 : 0;
        final int loops = (int) (ferocity - ferocity % 100) + conditionalLoop;
        final SkyblockDamage feroDamage = new SkyblockDamage(false, damage, isCriticalHit, DamageReason.FEROCITY, this, target);

        AtomicInteger i = new AtomicInteger();
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            i.getAndIncrement();
            if (i.get() == loops || target.getEntity().getInstance() == null) return TaskSchedule.stop();
            EventDispatcher.call(new SkyblockDamageEvent(feroDamage));
            playFerocity();
            return TaskSchedule.millis(50);
        });
    }


    @Override
    public void applyDamage(@NotNull SkyblockDamage damage) {
        if (isInvulnerable) return;
        Entity source = damage.source().getEntity();

        if (source instanceof SkyblockEntity) {
            applyKnockBack(source);
            double absoluteDamage = damage.damage();
            double playerDefense = statsHandler.getStat(Statistic.DEFENSE);
            double damageToBeTaken =  absoluteDamage * (1 - (playerDefense/(playerDefense + 100)));
            statsHandler.subtractCurrentHealth(damageToBeTaken);
            sendPackets();
        }
    }

    @Override
    public void kill() {
        teleport(island.spawn());
        looseCoinsOnDeath();
        statsHandler.healMana();
        statsHandler.healHealth();
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
            case HEALTH -> statsHandler.subtractCurrentHealth(abilityCost);
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
        actionBar.setDefaultDisplay(ActionBarSection.HEALTH, Component.text(df.format(statsHandler.getCurrentHealth()) + "/" + df.format(statsHandler.getStat(Statistic.HEALTH)) + "❤", RED));
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

    private void looseCoinsOnDeath() {
        long lostAmount = coins / 2;
        if (lostAmount == 0) return;
        sendMessage(MiniString.toComponent("<red>You died and lost <coins> coins.", Placeholder.parsed("coins", String.valueOf(lostAmount))));
        setCoins(coins - lostAmount);
    }

    private void playFerocity() {
        Sound FEROCITY = Sound.sound(Key.key("entity.iron_golem.attack"), Sound.Source.PLAYER, 1, 1.5f);
        Sound FEROCITY2 = Sound.sound(Key.key("entity.zombie.break_wooden_door"), Sound.Source.PLAYER, 1, 1.3f);
        Sound FEROCITY3 = Sound.sound(Key.key("item.flintandsteel.use"), Sound.Source.PLAYER, 1, 0.5f);
        Sound FEROCITY4 = Sound.sound(Key.key("entity.player.hurt"), Sound.Source.PLAYER, 1, 1f);
        playSound(FEROCITY3);
        playSound(FEROCITY4);
        playSound(FEROCITY);
        playSound(FEROCITY2);
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

    public void addCoins(long coins) {
        this.coins += coins;
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
