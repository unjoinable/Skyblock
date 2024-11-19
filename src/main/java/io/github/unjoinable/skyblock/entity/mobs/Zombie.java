package io.github.unjoinable.skyblock.entity.mobs;

import io.github.unjoinable.skyblock.entity.SkyblockEntity;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
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
    public double getHealth(int lvl) {
        return Math.floor(100 * Math.pow(Math.E, (lvl - 1) * 0.1398));
    }

    @Override
    public double getDefense(int lvl) {
        return 0;
    }

    @Override
    public double getBaseDamage(int lvl) {
        return Math.floor((20.16 * Math.pow(Math.E, lvl * 0.122)));
    }

    @Override
    public double getSpeed(int lvl) {
        return 20;
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
                new ClosestEntityTarget(this, 16, entity -> entity instanceof SkyblockPlayer)
        );
    }
}
