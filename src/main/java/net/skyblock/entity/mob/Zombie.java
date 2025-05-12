package net.skyblock.entity.mob;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.skyblock.entity.base.SkyblockEntity;
import net.skyblock.player.SkyblockPlayer;
import net.skyblock.stats.calculator.StatProfile;
import net.skyblock.stats.definition.StatValueType;
import net.skyblock.stats.definition.Statistic;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

public class Zombie extends SkyblockEntity {

    public Zombie() {
        super(EntityType.ZOMBIE);
    }

    @Override
    public @NotNull String name() {
        return "Zombie";
    }

    @Override
    public List<GoalSelector> getGoalSelectors(int lvl) {
        return List.of(
                new FollowTargetGoal(this, Duration.ofSeconds(1)),
                new MeleeAttackGoal(this, 1.6, 20, TimeUnit.SERVER_TICK),
                new RandomStrollGoal(this, 16)
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors(int lvl) {
        return List.of(
                new LastEntityDamagerTarget(this, 16), // how dare you
                new ClosestEntityTarget(this, 16, SkyblockPlayer.class::isInstance)
        );
    }

    @Override
    protected void configureStats(int lvl) {
        double health = Math.floor(100 * Math.pow(Math.E, (lvl - 1) * 0.1398));
        double damage = Math.floor(20.16 * Math.pow(Math.E, lvl * 0.122));
        double speed = 20.0;

        StatProfile stats = getStatProfile();
        stats.addStat(Statistic.HEALTH, StatValueType.BASE, health);
        stats.addStat(Statistic.DAMAGE, StatValueType.BASE, damage);
        stats.addStat(Statistic.SPEED, StatValueType.BASE, speed);
    }
}
