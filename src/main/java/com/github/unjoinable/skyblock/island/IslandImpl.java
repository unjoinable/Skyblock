package com.github.unjoinable.skyblock.island;

import com.github.unjoinable.skyblock.util.NamespacedId;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an implementation of the Island interface.
 *
 * @param id The unique identifier of the island.
 * @param displayName The display name of the island.
 * @param spawn The position of the island's spawn point.
 * @see Island
 */
public record IslandImpl(@NotNull NamespacedId id,
                         @NotNull String displayName,
                         @NotNull Pos spawn) implements Island {
}
