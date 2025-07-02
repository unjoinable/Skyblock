package net.unjoinable.skyblock.event.listener;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.entity.SkyblockEntity;
import net.unjoinable.skyblock.player.SkyblockPlayer;

public class EntityListener {
    private final GlobalEventHandler eventHandler;

    public EntityListener(GlobalEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void register() {
        registerEntityAttackListener();
    }

    private void registerEntityAttackListener() {
        eventHandler.addListener(EntityAttackEvent.class, event -> {
            Entity target = event.getTarget();
            Entity damager = event.getEntity();

            SkyblockDamage damage = switch (damager) {
                case SkyblockPlayer player -> player.getCombatSystem().attack(target);
                case SkyblockEntity entity -> entity.attackMelee(damager);
                default -> null;
            };

            if (damage == null) return;

            switch (target) {
                case SkyblockPlayer player -> player.getCombatSystem().damage(damage);
                case SkyblockEntity entity -> entity.damage(damage);
                default -> {/*Do nothing for non-skyblock mob entity*/}
            }
        });
    }
}
