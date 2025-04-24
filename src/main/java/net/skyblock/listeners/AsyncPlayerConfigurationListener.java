package net.skyblock.listeners;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for handling player configuration during login asynchronously.
 * <p>
 * This listener sets the initial spawn instance and position for the player.
 */
public class AsyncPlayerConfigurationListener implements EventListener<AsyncPlayerConfigurationEvent> {
    private final Instance spawnInstance;
    private final Pos spawnPos;

    /**
     * Constructs the listener with a target instance and spawn position.
     *
     * @param spawnInstance the instance where players will spawn
     * @param spawnPos      the position in the instance where players will appear
     */
    public AsyncPlayerConfigurationListener(Instance spawnInstance, Pos spawnPos) {
        this.spawnInstance = spawnInstance;
        this.spawnPos = spawnPos;
    }

    /**
     * Gets the type of event this listener handles.
     *
     * @return {@link AsyncPlayerConfigurationEvent} class
     */
    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    /**
     * Sets the spawning instance and position for a player when they join.
     *
     * @param event the event triggered during player login configuration
     * @return {@link Result#SUCCESS} to continue event processing
     */
    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();
        event.setSpawningInstance(spawnInstance);
        player.setRespawnPoint(spawnPos);

        return Result.SUCCESS;
    }
}
