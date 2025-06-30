package net.unjoinable.skyblock.item.ability.impls;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.block.BlockIterator;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.ability.AbilityCostType;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.utils.MiniString;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;

public class InstantTransmission implements ItemAbility {
    private static final Key KEY = Key.key("ability:instant_transmission");
    private static final Component INVALID_TELEPORT = MiniString.asComponent("<red>There are blocks in the way!</red>");
    private static final List<Component> DESCRIPTION = MiniString
            .listBuilder()
            .add("<gray>Teleport <green>8 blocks ahead</green> of you and")
            .add("<gray>gain <green>+50</green><white> ✦ Speed</white> for <green>3 seconds</green>.")
            .build();

    @Override
    public @NotNull Key key() {
        return KEY;
    }

    @Override
    public String displayName() {
        return "Instant Transmission";
    }

    @Override
    public ExecutionType trigger() {
        return ExecutionType.RIGHT_CLICK;
    }

    @Override
    public AbilityCostType costType() {
        return AbilityCostType.MANA;
    }

    @Override
    public int cost() {
        return 50;
    }

    @Override
    public long cooldown() {
        return 50;
    }

    @Override
    public List<Component> description() {
        return DESCRIPTION;
    }

    @Override
    public BiConsumer<SkyblockPlayer, SkyblockItem> action() {
        return (player,  _) -> {
            Instance instance = player.getInstance();
            Pos pos = player.getPosition();
            Vec dir = getDirectionVector(pos.yaw(), pos.pitch());
            BlockIterator iterator = new BlockIterator(pos.asVec(), dir, player.getEyeHeight(), 8);

            Point teleportPos = pos;

            while (iterator.hasNext()) {
                Point point = iterator.next();

                if (!instance.getBlock(point).isAir()) {
                    player.sendMessage(INVALID_TELEPORT);
                    break;
                }
                teleportPos = point;
            }

            player.teleport(Pos.fromPoint(teleportPos).withYaw(pos.yaw()).withPitch(pos.pitch()));
        };
    }

    /**
     * Calculates a normalized direction vector from yaw and pitch angles
     * @param yaw The horizontal rotation in degrees (0° = south, 90° = west, etc.)
     * @param pitch The vertical rotation in degrees (0° = horizontal, -90° = up, +90° = down)
     * @return A normalized Vec direction vector
     */
    private static Vec getDirectionVector(float yaw, float pitch) {
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec(x, y, z);
    }
}