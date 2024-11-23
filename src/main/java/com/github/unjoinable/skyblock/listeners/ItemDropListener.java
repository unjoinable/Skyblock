package com.github.unjoinable.skyblock.listeners;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class ItemDropListener implements EventListener<ItemDropEvent> {
    public static final Tag<SkyblockPlayer> DROPPED_BY_PLAYER = Tag.Transient("dropped_by_player");

    @Override
    public @NotNull Class<ItemDropEvent> eventType() {
        return ItemDropEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull ItemDropEvent event) {
        ItemStack item = event.getItemStack();
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();

        item = item.withTag(DROPPED_BY_PLAYER, player);
        ItemEntity entity = new ItemEntity(item);

        Pos pos = player.getPosition();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double sin = Math.sin(Math.toRadians(pos.yaw()));
        double cos = Math.cos(Math.toRadians(pos.pitch()));
        double tau = Math.PI*2;
        double deltaX = -sin * cos * 0.3F * Math.cos((random.nextFloat() * tau) * 0.02F * random.nextFloat());
        double deltaY = -Math.sin(Math.toRadians(pos.pitch())) * 0.3F + 0.1F + (random.nextFloat() - random.nextFloat()) * 0.1F;
        double deltaZ =  Math.cos(Math.toRadians(pos.yaw())) * cos * 0.3F + Math.sin(random.nextFloat() * tau * (double) 0.02F * random.nextFloat());
        Vec vec = new Vec(deltaX, deltaY, deltaZ).mul(20D);

        entity.setPickupDelay(Duration.ofSeconds(2L));
        entity.setAerodynamics(entity.getAerodynamics().withHorizontalAirResistance(0.96));
        entity.setInstance(event.getInstance(), pos.add(0,player.getEyeHeight() - 0.3D,0));
        entity.setVelocity(vec);
        return Result.SUCCESS;
    }
}
