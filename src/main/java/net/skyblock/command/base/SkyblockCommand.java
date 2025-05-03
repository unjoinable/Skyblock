package net.skyblock.command.base;

import net.skyblock.player.rank.PlayerRank;
import net.skyblock.player.SkyblockPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.CommandCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for all Skyblock commands with rank-based permissions.
 * Automatically handles permission checks based on player ranks.
 */
public abstract class SkyblockCommand extends Command {
    /**
     * Creates a new Skyblock command with the specified name and aliases.
     * Automatically applies rank-based permission checks.
     *
     * @param name    the primary name of the command
     * @param aliases alternative names for the command
     */
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
     */
    private record RankCondition(PlayerRank requiredRank) implements CommandCondition {

        /**
         * Creates a new rank condition.
         *
         * @param requiredRank the minimum rank required to pass the check
         */
        private RankCondition {}

        @Override
        public boolean canUse(@NotNull CommandSender sender, @Nullable String commandString) {
            return sender instanceof SkyblockPlayer player && player.getPlayerRank().isAtLeast(requiredRank);
        }
    }
}