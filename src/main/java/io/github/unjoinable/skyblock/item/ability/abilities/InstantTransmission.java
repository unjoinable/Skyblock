package io.github.unjoinable.skyblock.item.ability.abilities;

import io.github.unjoinable.skyblock.item.ability.AbilityCostType;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.item.ability.AbilityType;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.block.BlockIterator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InstantTransmission implements Ability {
    private static final NamespacedId NAMESPACED_ID = new NamespacedId("ability", "instant_transmission");
    private static final List<Component> LORE = new ArrayList<>();

    @Override
    public @NotNull String name() {
        return "Instant Transmission";
    }

    @Override
    public @NotNull NamespacedId id() {
        return NAMESPACED_ID;
    }

    @Override
    public @NotNull AbilityCostType costType() {
        return AbilityCostType.MANA;
    }

    @Override
    public @NotNull AbilityType type() {
        return AbilityType.RIGHT_CLICK;
    }

    @Override
    public int abilityCost() {
        return 50;
    }

    @Override
    public long cooldownInMs() {
        return 50;
    }

    @Override
    public @NotNull List<Component> lore() {
        return LORE;
    }

    @Override
    public void run(@NotNull SkyblockPlayer player, @NotNull SkyblockItem item) {
        Pos playerPos = player.getPosition();
        Vec dir = playerPos.direction();

        BlockIterator iterator = new BlockIterator(playerPos.asVec(), dir, 1, 8);
        Pos finalPos = playerPos;
        while (iterator.hasNext()) {
            Point a = iterator.next();
            Pos targetLoc = Pos.fromPoint(a).withYaw(playerPos.yaw()).withPitch(playerPos.pitch()).add(0.5D, 0D, 0.5D);
            if (!player.getInstance().getBlock(a).isAir() && !player.getInstance().getBlock(a).isAir()) {
                break;
            } else {
                finalPos = targetLoc;
            }
        }
        player.teleport(finalPos);
    }
}
