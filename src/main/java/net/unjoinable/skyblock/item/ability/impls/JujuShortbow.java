package net.unjoinable.skyblock.item.ability.impls;

import net.kyori.adventure.key.Key;
import net.minestom.server.coordinate.Pos;
import net.unjoinable.skyblock.entity.projectile.Arrow;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.ability.AbilityCostType;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.item.ability.traits.ShortbowAbility;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class JujuShortbow implements ShortbowAbility {
    private static final Key KEY = Key.key("ability:juju_shortbow");

    @Override
    public ExecutionType trigger() {
        return ExecutionType.LEFT_CLICK;
    }

    @Override
    public AbilityCostType costType() {
        return AbilityCostType.FREE;
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public BiConsumer<SkyblockPlayer, SkyblockItem> action() {
        return (player, _) -> {
            Arrow projectile = new Arrow(player);
            Pos shootPosition = player.getPosition().add(0, player.getEyeHeight()-0.1, 0);
            projectile.shoot(shootPosition.asVec(), 3f, 1f);
        };
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }
}
