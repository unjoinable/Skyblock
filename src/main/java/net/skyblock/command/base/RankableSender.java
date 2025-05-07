package net.skyblock.command.base;

import net.skyblock.player.rank.PlayerRank;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an entity that can be assigned a {@link PlayerRank}.
 * This interface is used to standardize access to rank-related data
 * across different types of senders, such as players or console senders.
 */
public interface RankableSender {

    /**
     * Gets the {@link PlayerRank} associated with this sender.
     *
     * @return the rank of the sender
     */
    @NotNull PlayerRank getRank();
}
