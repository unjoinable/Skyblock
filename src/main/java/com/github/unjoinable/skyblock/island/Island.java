package com.github.unjoinable.skyblock.island;

import com.github.unjoinable.skyblock.util.NamespacedObject;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

public interface Island extends NamespacedObject, Islands {

    /**
     * Retrieves the display name of the island.
     *
     * @return A String representing the display name of the island.
     */
    @NotNull String displayName();

    /**
     * Provides the spawn position of the island.
     *
     * @return A {@link Pos} representing the spawn position of the island.
     */
    @NotNull Pos spawn();
}
