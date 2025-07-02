package net.unjoinable.skyblock.item.ability.impls;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.unjoinable.skyblock.combat.damage.SkyblockDamage;
import net.unjoinable.skyblock.entity.SkyblockEntity;
import net.unjoinable.skyblock.item.SkyblockItem;
import net.unjoinable.skyblock.item.ability.AbilityCostType;
import net.unjoinable.skyblock.item.ability.ExecutionType;
import net.unjoinable.skyblock.item.ability.MagicAbility;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.utils.MiniString;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;

public class WitherImpact implements MagicAbility {
    private static final Key KEY = Key.key("ability:wither_impact");
    private static final List<Component> DESCRIPTION = MiniString
            .listBuilder()
            .add("<gray>Teleport <green>10 blocks</green><gray> ahead of you.</gray>")
            .add("<gray>Then implode dealing <red>10,000</red><gray> damage")
            .add("<gray>to nearby enemies. Also applies the")
            .add("<gray>wither shield scroll ability reducing")
            .add("<gray>damage taken and granting an")
            .add("<gray>absorption shield for <yellow>5 seconds</yellow><gray>.</gray>")
            .build();

    @Override
    public int baseAbilityDamage() {
        return 10000;
    }

    @Override
    public double abilityScalling() {
        return 0.3;
    }

    @Override
    public String displayName() {
        return "Wither Impact";
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
        return 300;
    }

    @Override
    public long cooldown() {
        return 150;
    }

    @Override
    public List<Component> description() {
        return DESCRIPTION;
    }

    @Override
    public BiConsumer<SkyblockPlayer, SkyblockItem> action() {
        return (player, _) -> {
            Instance instance = player.getInstance();
            Point center = player.getPosition();
            double range = 10;

            instance.getNearbyEntities(center, range)
                    .stream()
                    .filter(SkyblockEntity.class::isInstance)
                    .map(SkyblockEntity.class::cast)
                    .forEach(entity -> {
                        SkyblockDamage dmg = player.getCombatSystem().magicAttack(entity, this);
                        entity.damage(dmg);
                    });
        };
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }
}
