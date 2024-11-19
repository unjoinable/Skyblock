package io.github.unjoinable.skyblock.statistics;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents damage dealt in the Skyblock game, including details about the type of damage,
 * the amount, and the entities involved.
 *
 * @param isMagicDamage     Indicates whether the damage is magical.
 * @param damage            The amount of damage dealt.
 * @param isCriticalDamage  Indicates whether the damage is critical.
 * @param reason            The reason or type of damage.
 * @param source            The entity that dealt the damage.
 * @param target            The entity that received the damage.
 */
public record SkyblockDamage(
        boolean isMagicDamage,
        double damage,
        boolean isCriticalDamage,
        @NotNull DamageReason reason,
        @NotNull CombatEntity source,
        @NotNull CombatEntity target) {

    @NotNull Damage toMinestom() {
        return new Damage(
                DamageType.GENERIC,
                target.getEntity(),
                source.getEntity(),
                target.getEntity().getPosition(),
                (float) damage);
    }
}
