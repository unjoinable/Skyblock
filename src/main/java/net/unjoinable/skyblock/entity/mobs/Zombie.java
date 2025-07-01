package net.unjoinable.skyblock.entity.mobs;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.unjoinable.skyblock.entity.SkyblockEntity;
import net.unjoinable.skyblock.player.SkyblockPlayer;

import java.time.Duration;
import java.util.List;

import static net.unjoinable.skyblock.combat.statistic.StatValueType.BASE;
import static net.unjoinable.skyblock.combat.statistic.Statistic.*;

public class Zombie extends SkyblockEntity {

    public Zombie(int level) {
        super(EntityType.ZOMBIE, level);
    }

    @Override
    public String name() {
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

        statProfile.addStat(HEALTH, BASE, health);
        statProfile.addStat(DAMAGE, BASE, damage);
        statProfile.addStat(SPEED, BASE, speed);
    }
}
