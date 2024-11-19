package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.entity.Hologram;
import io.github.unjoinable.skyblock.entity.holograms.DamageIndicator;
import io.github.unjoinable.skyblock.entity.mobs.Zombie;
import io.github.unjoinable.skyblock.gui.inventories.SkyblockMenu;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.timer.TaskSchedule;

public class TestCommand extends Command {

    public TestCommand() {
        super("test");

        addSyntax((sender, context) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);

            Sound FEROCITY = Sound.sound(Key.key("entity.iron_golem.attack"), Sound.Source.PLAYER, 1, 1.5f);
            Sound FEROCITY2 = Sound.sound(Key.key("entity.zombie.break_wooden_door"), Sound.Source.PLAYER, 1, 1.3f);
            Sound FEROCITY3 = Sound.sound(Key.key("item.flintandsteel.use"), Sound.Source.PLAYER, 1, 0.5f);
            Sound FEROCITY4 = Sound.sound(Key.key("entity.player.hurt"), Sound.Source.PLAYER, 1, 1f);
            player.playSound(FEROCITY3);
            player.playSound(FEROCITY3);
            player.playSound(FEROCITY3);
            player.playSound(FEROCITY3);

            player.playSound(FEROCITY4);
            player.playSound(FEROCITY);
            player.playSound(FEROCITY);


            player.playSound(FEROCITY2);
        });
    }
}
