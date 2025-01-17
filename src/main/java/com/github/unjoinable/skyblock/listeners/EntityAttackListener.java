package com.github.unjoinable.skyblock.listeners;

import com.github.unjoinable.skyblock.statistics.CombatEntity;
import com.github.unjoinable.skyblock.statistics.DamageReason;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.entity.EntityAttackEvent;
import org.jetbrains.annotations.NotNull;

public class EntityAttackListener implements EventListener<EntityAttackEvent> {

    @Override
    public @NotNull Class<EntityAttackEvent> eventType() {
        return EntityAttackEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull EntityAttackEvent entityAttackEvent) {
        Entity target = entityAttackEvent.getTarget();
        Entity attacker = entityAttackEvent.getEntity();

        if (target instanceof CombatEntity && attacker instanceof CombatEntity) {
            ((CombatEntity) attacker).meleeDamage((CombatEntity) target, DamageReason.MELEE);
        }

        return Result.SUCCESS;
    }
}
