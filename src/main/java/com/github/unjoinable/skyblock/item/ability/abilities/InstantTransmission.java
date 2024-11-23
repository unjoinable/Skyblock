package com.github.unjoinable.skyblock.item.ability.abilities;

import com.github.unjoinable.skyblock.item.ability.AbilityCostType;
import com.github.unjoinable.skyblock.item.SkyblockItem;
import com.github.unjoinable.skyblock.item.ability.Ability;
import com.github.unjoinable.skyblock.item.ability.AbilityType;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import com.github.unjoinable.skyblock.util.MiniString;
import com.github.unjoinable.skyblock.util.NamespacedId;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.block.BlockIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InstantTransmission implements Ability {
    private static final NamespacedId NAMESPACED_ID = new NamespacedId("ability", "instant_transmission");
    private static final List<Component> LORE = new ArrayList<>();

    static {
        LORE.add(MiniString.toComponent("Teleport <green>8 blocks</green> ahead of you and"));
        LORE.add(MiniString.toComponent("gain <green>+50</green> for <white>✦ Speed</white> <green>3 seconds</green>"));
    }

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
    public int abilityCost(@Nullable SkyblockPlayer player) {
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
