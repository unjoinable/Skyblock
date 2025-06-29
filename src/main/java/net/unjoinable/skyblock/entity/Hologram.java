package net.unjoinable.skyblock.entity;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.timer.TaskSchedule;

/**
 * Represents a floating text display entity in the world.
 */
public class Hologram extends Entity {
    protected final TextDisplayMeta meta;
    protected Component text;

    /**
     * Creates a new hologram with the specified text.
     *
     * @param text The text to display
     */
    public Hologram(Component text) {
        super(EntityType.TEXT_DISPLAY);
        this.meta = (TextDisplayMeta) getEntityMeta();
        this.text = text;
    }

    /**
     * Updates the displayed text.
     *
     * @param text The new text to display
     * @return This hologram instance for chaining
     */
    public Hologram setText(Component text) {
        this.text = text;
        meta.setText(text);
        return this;
    }

    /**
     * Schedules this hologram to be removed after a delay.
     *
     * @param removalInMs Milliseconds until removal
     * @return This hologram instance for chaining
     */
    public Hologram scheduleRemoval(long removalInMs) {
        if (removalInMs > 0) {
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (isActive()) {
                    remove();
                }
            }, TaskSchedule.millis(removalInMs), TaskSchedule.stop());
        }
        return this;
    }

    @Override
    public void spawn() {
        meta.setText(text);
        meta.setHasNoGravity(true);
        meta.setSeeThrough(false);
        meta.setUseDefaultBackground(true);
        meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
        super.spawn();
    }
}