package com.github.unjoinable.skyblock.user;

import com.github.unjoinable.skyblock.entity.SkyblockEntity;
import com.github.unjoinable.skyblock.island.Island;
import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.item.ability.AbilityCostType;
import com.github.unjoinable.skyblock.skill.Skill;
import com.github.unjoinable.skyblock.statistics.CombatEntity;
import com.github.unjoinable.skyblock.statistics.DamageReason;
import com.github.unjoinable.skyblock.statistics.SkyblockDamage;
import com.github.unjoinable.skyblock.statistics.Statistic;
import com.github.unjoinable.skyblock.user.actionbar.ActionBar;
import com.github.unjoinable.skyblock.user.actionbar.ActionBarDisplayReplacement;
import com.github.unjoinable.skyblock.user.actionbar.ActionBarPurpose;
import com.github.unjoinable.skyblock.user.actionbar.ActionBarSection;
import com.github.unjoinable.skyblock.util.MiniString;
import com.github.unjoinable.skyblock.util.NamespacedId;
import com.github.unjoinable.skyblock.util.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.timer.ExecutionType;
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
    // Static components for action bar messages
    private static final Component NOT_ENOUGH_MANA = Component.text("NOT ENOUGH MANA", RED, BOLD).decoration(ITALIC, false);
    private static final ActionBarDisplayReplacement NOT_ENOUGH_MANA_REPLACEMENT = new ActionBarDisplayReplacement(
            NOT_ENOUGH_MANA,
            40,
            10,
            ActionBarPurpose.ABILITY
    );

    static {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            for (Player serverPlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!serverPlayer.isPlayer()) continue;
                SkyblockPlayer player = ((SkyblockPlayer) serverPlayer);
                player.taskLoop();
            }
            return TaskSchedule.seconds(1L);
        }, ExecutionType.TICK_END);
    }

    private final ActionBar actionBar;
    private final StatisticsHandler statsHandler;
    private final AbilityHandler abilityHandler;

    private boolean isInvulnerable;

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
    public void meleeDamage(@NotNull CombatEntity target, DamageReason reason) {
        double baseDamage = statsHandler.getStat(Statistic.DAMAGE);
        double strength = statsHandler.getStat(Statistic.STRENGTH);
        double critDamage = statsHandler.getStat(Statistic.CRIT_DAMAGE);
        double critChance = statsHandler.getStat(Statistic.CRIT_CHANCE);

        double damage = (5 + baseDamage) * (1 + strength/100);

        boolean isCriticalHit = critChance >= 100 || Utils.probabilityCheck((int) critChance);

        if (isCriticalHit) {
            damage *= critDamage;
        }
        target.applyDamage(new SkyblockDamage(false, damage, isCriticalHit, reason, this,target));

        //ferocity
        double ferocity = statsHandler.getStat(Statistic.FEROCITY);
        if (ferocity == 0) return;
        int conditionalLoop = Utils.probabilityCheck((int) (ferocity % 100)) ? 1 : 0;
        final int loops = (int) (ferocity - ferocity % 100) + conditionalLoop;
        final SkyblockDamage feroDamage = new SkyblockDamage(false, damage, isCriticalHit, DamageReason.FEROCITY, this, target);

        AtomicInteger i = new AtomicInteger();
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (i.get() == loops || target.getEntity().getInstance() == null) return TaskSchedule.stop();
            target.applyDamage(feroDamage);
            i.getAndIncrement();
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
