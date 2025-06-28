package net.unjoinable.skyblock.item.ability.impls;

import net.kyori.adventure.text.Component;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.ability.AbilityCostType;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.item.ability.ItemAbility;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.utils.MiniString;
import net.unjoinable.skyblock.utils.NamespaceId;

import java.util.List;
import java.util.function.BiConsumer;

public class InstantTransmission implements ItemAbility {
    private static final NamespaceId ID = NamespaceId.fromString("ability:instant_transmission");
    private static final List<Component> DESCRIPTION = MiniString
            .listBuilder()
            .add("<gray>Teleport <green>8 blocks ahead</green> of you and")
            .add("<gray>gain <green>+50</green><white> ✦ Speed</white> for <green>3 seconds</green>.")
            .build();

    @Override
    public NamespaceId id() {
        return ID;
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
        return (skyblockPlayer, _) -> {
            // marker
            skyblockPlayer.sendMessage("u kinda smell icl");
        };
    }
}
