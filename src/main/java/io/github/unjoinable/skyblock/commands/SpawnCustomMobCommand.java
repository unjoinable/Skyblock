package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.entity.SkyblockEntity;
import io.github.unjoinable.skyblock.entity.SkyblockMob;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;

public class SpawnCustomMobCommand extends Command {

    public SpawnCustomMobCommand() {
        super("spawncustommob", "scm");

        //args
        ArgumentEnum<SkyblockMob> mobArgument = ArgumentType.Enum("mob", SkyblockMob.class);
        ArgumentInteger levelArg = ArgumentType.Integer("level");

        addSyntax((sender, context) -> {
            SkyblockPlayer player = ((SkyblockPlayer) sender);
            SkyblockEntity entity = context.get(mobArgument).get();
            int level = context.get(levelArg);

            entity.spawn(level, player.getInstance(), player.getPosition());
        }, mobArgument, levelArg);
    }
}
