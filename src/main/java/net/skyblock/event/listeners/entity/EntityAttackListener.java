package net.skyblock.event.listeners.entity;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.skyblock.stats.holder.CombatEntity;
import net.skyblock.stats.definition.DamageType;
import org.jetbrains.annotations.NotNull;

public class EntityAttackListener implements EventListener<EntityAttackEvent> {

    @Override
    public @NotNull Class<EntityAttackEvent> eventType() {
        return EntityAttackEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull EntityAttackEvent event) {
        if (event.getEntity() instanceof CombatEntity source &&
                event.getTarget() instanceof  CombatEntity target) {
            source.attack(target, DamageType.MELEE);
        }

        return Result.SUCCESS;
    }
}
