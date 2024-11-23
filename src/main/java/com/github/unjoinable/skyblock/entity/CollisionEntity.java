package com.github.unjoinable.skyblock.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.timer.TaskSchedule;

import java.util.HashSet;
import java.util.Set;

public abstract class CollisionEntity extends LivingEntity {
    private final Set<SkyblockEntity> entities = new HashSet<>();
    private final long delayInMs;

    public CollisionEntity(EntityType type, long delayInMs) {
        super(type);
        this.delayInMs = delayInMs;
        this.setNoGravity(true);
    }

    @Override
    public void spawn() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            collisionCheck();
            return TaskSchedule.millis(delayInMs);
        });
    }

    public void onCollide(SkyblockEntity entity) {
        entities.add(entity);
    }

    public void collisionCheck() {
        CollisionUtils.checkEntityCollisions(
                this,
                this.getVelocity(),
                5,
                entity -> {
                    return entity instanceof SkyblockEntity && !entities.contains(entity);
                },
                null
        ).forEach(entity -> onCollide((SkyblockEntity) entity.entity()));
    }

    public long getDelayInMs() {
        return delayInMs;
    }
}
