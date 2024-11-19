package io.github.unjoinable.skyblock.entity;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.timer.TaskSchedule;

public class Hologram extends Entity {
    private final TextDisplayMeta meta;
    private Component text;
    private boolean scheduledForRemoval = false;
    private long removalInMs;

    public Hologram(Component text) {
        super(EntityType.TEXT_DISPLAY);
        this.meta = (TextDisplayMeta) getEntityMeta();
        this.text = text;
    }

    public void setText(Component text) {
        this.text = text;
        meta.setText(text);
    }

    public void scheduleRemoval(long removalInMs) {
        this.scheduledForRemoval = true;
        this.removalInMs = removalInMs;
    }

    @Override
    public void spawn() {
        meta.setText(text);
        meta.setHasNoGravity(true);
        meta.setSeeThrough(false);
        meta.setUseDefaultBackground(true);
        meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);

        if (scheduledForRemoval) {
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (isActive()) {
                    remove();
                }
            }, TaskSchedule.millis(removalInMs), TaskSchedule.stop());
        }
    }
}
