package net.unjoinable.skyblock.command;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.unjoinable.skyblock.player.SkyblockPlayer;
import net.unjoinable.skyblock.player.rank.PlayerRank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SkyblockCommand extends Command {

    protected SkyblockCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);
        setCondition(new RankCondition(getRequiredRank()));
    }

    /**
     * Returns the minimum required rank to execute this command.
     *
     * @return the minimum PlayerRank needed to use this command
     */
    public abstract PlayerRank getRequiredRank();

    /**
     * Permission condition that checks if a sender has the required rank.
     * @param requiredRank the minimum rank required to pass the check
     */
    private record RankCondition(PlayerRank requiredRank) implements CommandCondition {

        @Override
        public boolean canUse(@NotNull CommandSender sender, @Nullable String commandString) {
            return sender instanceof SkyblockPlayer player && player.getPlayerRank().isAtLeast(requiredRank);
        }
    }
}
