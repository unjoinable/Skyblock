package com.github.unjoinable.skyblock.entity;

import com.github.unjoinable.skyblock.entity.holograms.DamageIndicator;
import com.github.unjoinable.skyblock.statistics.CombatEntity;
import com.github.unjoinable.skyblock.statistics.DamageReason;
import com.github.unjoinable.skyblock.statistics.SkyblockDamage;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.util.MiniString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SkyblockEntity extends EntityCreature implements CombatEntity {
    private static final List<SkyblockEntity> activeMobs = new ArrayList<>();
    private int level = 0;

    private double currentHealth;
    private boolean isInvulnerable = false;

    public SkyblockEntity(@NotNull EntityType entityType) {
        super(entityType);
    }

    public abstract @NotNull String name();

    public abstract double getHealth(int lvl);

    public abstract double getDefense(int lvl);

    public abstract double getBaseDamage(int lvl);

    public abstract double getSpeed(int lvl);

    public abstract List<GoalSelector> getGoalSelectors(int lvl);

    public abstract List<TargetSelector> getTargetSelectors(int lvl);

    public double getStrength(int lvl) {
        return 0D;
    }

    public void spawn(int lvl, Instance instance, Pos spawnPos) {
        setCustomNameVisible(true);
        level = lvl;
        currentHealth = getHealth(level);
        //health
        setCurrentHealth(getHealth(lvl));

        getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue((getSpeed(lvl) / 1000) * 2.5);
        setCustomName(displayName(lvl));
        setAutoViewable(true);
        setAutoViewEntities(true);
        addAIGroup(getGoalSelectors(lvl), getTargetSelectors(lvl));

        activeMobs.add(this);
        setInstance(instance, spawnPos);
    }

    public Component displayName(int lvl) {
        return MiniString.toComponent(
                "<dark_gray>[<gray>Lv<lvl></gray>] <red><name> <green><cur_health:'#'>/<total_health:'#'></green>❤",
                Placeholder.parsed("lvl", String.valueOf(lvl)),
                Placeholder.parsed("name", name()),
                Formatter.number("cur_health", currentHealth),
                Formatter.number("total_health", getHealth(lvl))
        );
    }

    @Override
    public @NotNull Entity getEntity() {
        return this;
    }

    public void subtractHealth(double damage) {
        setCurrentHealth(getCurrentHealth() - damage);
    }

    public int getLevel() {
        return level;
    }

    public double calcAbsoluteDamageDealt(int lvl) {
        double baseDamage = getBaseDamage(lvl);
        double strength = getStrength(lvl);

        return (5 + baseDamage) * (1 +  strength / 100);
    }

    @Override
    public void kill() {
        this.remove();
        activeMobs.remove(this);
    }

    @Override
    public double getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public void setCurrentHealth(double currentHealth) {
        if (currentHealth <= 0) kill();
        this.currentHealth = currentHealth;
    }

    @Override
    public double getMaxHealth() {
        return getHealth(level);
    }

    @Override
    public boolean isInvunerable() {
        return isInvulnerable;
    }

    @Override
    public void setInvunerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    @Override
    public void meleeDamage(@NotNull CombatEntity target, DamageReason reason) {
        target.applyDamage(new SkyblockDamage(false, calcAbsoluteDamageDealt(level), false, reason, this, target));
    }

    @Override
    public void applyDamage(@NotNull SkyblockDamage damage) {
        Entity source = damage.source().getEntity();

        if (source instanceof SkyblockPlayer) {
            if (damage.reason() == DamageReason.MELEE) {
                applyKnockBack(source);
            }
            double absoluteDamage = damage.damage();
            double entityDefense = getDefense(level);
            double damageToBeTaken =  absoluteDamage * (1 - (entityDefense/(entityDefense + 100)));
            new DamageIndicator(damageToBeTaken, damage.isCriticalDamage()).spawn(getPosition(), getInstance());
            sendPackets();
            subtractHealth(damageToBeTaken);
        }
        setCustomName(displayName(level));
    }

    public static boolean isAlive(UUID uuid) {
        for (SkyblockEntity activeMob : activeMobs) {
            if (activeMob.getUuid() == uuid) return true;
        }
        return false;
    }

    public static @Nullable SkyblockEntity getSkyblockInstance(UUID uuid) {
        for (SkyblockEntity activeMob : activeMobs) {
            if (activeMob.getUuid() == uuid) {
                return activeMob;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "SkyblockEntity{" +
                "level=" + level +
                ", currentHealth=" + currentHealth +
                ", isInvulnerable=" + isInvulnerable +
                '}';
    }
}
