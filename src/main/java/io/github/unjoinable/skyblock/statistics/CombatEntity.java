package io.github.unjoinable.skyblock.statistics;

import net.minestom.server.entity.Entity;
import net.minestom.server.network.packet.server.play.EntityAnimationPacket;
import org.jetbrains.annotations.NotNull;

public interface CombatEntity {

    Entity getEntity();

    double getCurrentHealth();

    void setCurrentHealth(double health);

    double getMaxHealth();

    default void setMaxHealth(double maxHealth) {}

    boolean isInvunerable();

    void setInvunerable(boolean invulnerable);

    void meleeDamage(@NotNull CombatEntity target);

    void applyDamage(@NotNull SkyblockDamage damage);

    void kill();

    default void applyKnockBack(Entity sourceEntity) {
        float yaw = sourceEntity.getPosition().yaw();
        double degrees = Math.PI / 180;
        this.getEntity().takeKnockback(0.4f, Math.sin(yaw * degrees), -Math.cos(yaw * degrees));
    }

    default void sendPackets() {
        Entity entity = this.getEntity();
        entity.sendPacketToViewersAndSelf(new EntityAnimationPacket(
                entity.getEntityId(), EntityAnimationPacket.Animation.TAKE_DAMAGE
        ));
    }
}
