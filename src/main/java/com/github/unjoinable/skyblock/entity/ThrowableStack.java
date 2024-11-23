package com.github.unjoinable.skyblock.entity;

import com.github.unjoinable.skyblock.statistics.DamageReason;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;

import java.util.concurrent.atomic.AtomicInteger;

public class ThrowableStack extends CollisionEntity {
    private final ArmorStandMeta meta;
    private final SkyblockPlayer player;

    public ThrowableStack(SkyblockPlayer player, Material material, long delayInMs) {
        super(EntityType.ARMOR_STAND, delayInMs);
        this.player = player;
        this.meta = (ArmorStandMeta) getEntityMeta();
        this.setInvisible(true);
        this.hasPhysics = false;
        this.setItemInMainHand(ItemStack.of(material));
        this.setBoundingBox(1,1,1);
    }


    @Override
    public void spawn() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            collisionCheck();
            return TaskSchedule.millis(getDelayInMs());
        });
    }

    @Override
    public void onCollide(SkyblockEntity entity) {
        super.onCollide(entity);
        player.meleeDamage(entity, DamageReason.ABILITY);
    }

    public void push(SkyblockPlayer player) {
        Vec dir = player.getPosition().direction();
        AtomicInteger a = new AtomicInteger(10);
        MinecraftServer.getSchedulerManager().scheduleTask(
                () -> {
                    a.addAndGet(30);
                    meta.setRightArmRotation(new Vec(a.get(),0,0));
                    setVelocity(dir.mul(10));
                }, TaskSchedule.immediate(), TaskSchedule.millis(50L)
        );
    }
}
