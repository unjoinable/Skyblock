package io.github.unjoinable.skyblock.commands;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NBTCommand extends Command {

    public NBTCommand(@NotNull String name) {
        super(name);

        addSyntax((sender, context) -> {
            ItemStack item = ((SkyblockPlayer) sender).getItemInMainHand();
            System.out.println(item.toItemNBT());
        });
    }

}
