package net.skyblock.player.component;

import net.skyblock.component.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for components that can be attached to players.
 * Provides lifecycle methods for when the component is added to or removed from a player,
 * as well as an update method that runs every tick.
 */
public interface PlayerComponent extends Component {
    /**
     * Called when this component is added to a player.
     * Implementations can use this method to initialize component state.
     */
    default void onAdd() {} // Default empty implementation

    /**
     * Called when this component is removed from a player.
     * Implementations can use this method to clean up resources.
     */
    default void onRemove() {}  // Default empty implementation

    /**
     * Called every tick while this component is attached to a player.
     * Implementations should keep processing lightweight to avoid performance issues.
     */
    default void tick() {} // Default empty implementation

    @Override
    default @NotNull Class<? extends PlayerComponent> getType() {
        return getClass();
    }
}