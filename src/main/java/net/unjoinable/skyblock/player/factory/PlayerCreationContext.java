package net.unjoinable.skyblock.player.factory;

import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.unjoinable.skyblock.item.service.ItemProcessor;
import net.unjoinable.skyblock.time.SkyblockStandardTime;

/**
 * Represents all required context to create a Player instance in the game.
 * This includes the underlying Minestom connection details and game-specific logic.
 *
 * <p>This record is intended to be used as a clean parameter object for
 * Player creation factories or services.</p>
 *
 * @param connection    the player connection to the server (network layer)
 * @param gameProfile   the Minestom game profile containing username, UUID, properties
 * @param itemProcessor a service to manage the player's item-related logic
 * @param skyblockTime  the game's time system for tracking skyblock time
 */
public record PlayerCreationContext(
        PlayerConnection connection,
        GameProfile gameProfile,
        ItemProcessor itemProcessor,
        SkyblockStandardTime skyblockTime) {
    /**
     * Creates a new {@link Builder} instance to construct a {@link PlayerCreationContext}.
     *
     * @return a new Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A static builder class for {@link PlayerCreationContext}.
     * Ensures all required fields are set and not null before construction.
     */
    public static class Builder {
        private PlayerConnection connection;
        private GameProfile gameProfile;
        private ItemProcessor itemProcessor;
        private SkyblockStandardTime skyblockTime;

        public Builder connection(PlayerConnection connection) {
            this.connection = connection;
            return this;
        }

        public Builder gameProfile(GameProfile gameProfile) {
            this.gameProfile = gameProfile;
            return this;
        }

        public Builder itemProcessor(ItemProcessor itemProcessor) {
            this.itemProcessor = itemProcessor;
            return this;
        }

        public Builder skyblockTime(SkyblockStandardTime skyblockTime) {
            this.skyblockTime = skyblockTime;
            return this;
        }

        /**
         * Builds a {@link PlayerCreationContext} after validating all required fields are non-null.
         *
         * @return the constructed {@link PlayerCreationContext}
         * @throws IllegalStateException if any field is null
         */
        public PlayerCreationContext build() {
            if (this.connection == null) {
                throw new IllegalStateException("PlayerConnection must not be null.");
            }
            if (this.gameProfile == null) {
                throw new IllegalStateException("GameProfile must not be null.");
            }
            if (this.itemProcessor == null) {
                throw new IllegalStateException("ItemProcessor must not be null.");
            }
            if (this.skyblockTime == null) {
                throw new IllegalStateException("SkyblockStandardTime must not be null.");
            }
            return new PlayerCreationContext(connection, gameProfile, itemProcessor, skyblockTime);
        }
    }
}
