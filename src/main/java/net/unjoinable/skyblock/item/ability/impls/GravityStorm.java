package net.unjoinable.skyblock.item.ability.impls;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.ability.AbilityCostType;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.utils.MiniString;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;

public class GravityStorm implements ItemAbility {
    public static final Key KEY = Key.key("ability:gravity_storm");
    private static final List<Component> DESCRIPTION = MiniString
            .listBuilder()
            .add("<gray>Create a large <light_purple>rift</light_purple><gray> at the aimed")
            .add("<gray>location, pulling all mobs together.</gray>")
            .add("<dark_gray>Regen mana 10x slow for 3s after cast.")
            .build();

    @Override
    public String displayName() {
        return "Gravity Storm";
    }

    @Override
    public ExecutionType trigger() {
        return ExecutionType.LEFT_CLICK;
    }

    @Override
    public AbilityCostType costType() {
        return AbilityCostType.MANA;
    }

    @Override
    public int cost() {
        return 1200;
    }

    @Override
    public long cooldown() {
        return 100;
    }

    @Override
    public List<Component> description() {
        return DESCRIPTION;
    }

    @Override
    public BiConsumer<SkyblockPlayer, SkyblockItem> action() {
        return (player, item) -> {
            Point centre = player.getTargetBlockPosition(10);
            if (centre == null) return;


        };
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }
}
