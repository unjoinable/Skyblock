package io.github.unjoinable.skyblock.item.ability.abilities;

import io.github.unjoinable.skyblock.enums.AbilityCostType;
import io.github.unjoinable.skyblock.item.SkyblockItem;
import io.github.unjoinable.skyblock.item.ability.Ability;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.block.BlockIterator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.unjoinable.skyblock.util.MiniMessageTemplate.MM;

public class InstantTransmission implements Ability {
    private static final List<Component> LORE = new ArrayList<>();

    static {
        LORE.add(MM."§7Teleport §a8 blocks§7 ahead of");
        LORE.add(MM."§7you and gain §a+50 §f✦ Speed§7");
        LORE.add(MM."§7for §a3 seconds§7.");
    }

    @Override
    public @NotNull String name() {
        return "Instant Transmission";
    }

    @Override
    public @NotNull String id() {
        return "instant_transmission";
    }

    @Override
    public @NotNull AbilityCostType costType() {
        return AbilityCostType.MANA;
    }

    @Override
    public int abilityCost() {
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
