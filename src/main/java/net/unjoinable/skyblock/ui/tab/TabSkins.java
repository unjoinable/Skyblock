package net.unjoinable.skyblock.ui.tab;

import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;

import java.util.List;

/**
 * Utility class containing predefined Minecraft player skin data for use in tab list UI components.
 *
 * <p>This class provides static constants for different types of tab list slots, each containing
 * the necessary texture and signature data to display custom skins in the player tab list.
 * The skin data is Base64-encoded and includes cryptographic signatures for authentication.</p>
 *
 * <p>This is a utility class and cannot be instantiated.</p>
 */
class TabSkins {

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws AssertionError always, to indicate this class should not be instantiated
     */
    private TabSkins() {
        throw new AssertionError();
    }

    /**
     * Skin data for empty/blank tab list slots.
     */
    public static final List<PlayerInfoUpdatePacket.Property> EMPTY_SLOT = List.of(
            new PlayerInfoUpdatePacket.Property("textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTcwODY0NTgxMjM2MywKICAicHJvZmlsZUlkIiA6ICJkZDNjZGJiOTE2M2Q0NzgyOGQ0YmZkODZmYWE4NGY5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJFTFdhbEtlUkpBSkEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUzZWMyN2RiNzViYWNhYjkyN2U0ZmY5OGE5N2Q0YWZiNWYwZjE0NzcyZDc3YWY2Y2I2NzQ1ZGRkZjMzNDhhMyIKICAgIH0KICB9Cn0=",
                    "X/hd4jg3gYUrNmLXawvoi7D+ZK/u8FclyShFDj7EX1JepaNMnUp+Xor3QnEl6LqQq8v2021VxD63QlA58HPGZN2qUo9EmUgU3fkOjsjx9lZK/Kc1LH39pqohBFbqkrg3qk/9DMbZWH7dul82USsml+dd4N+5YE63JGPycKifEBjSQNijgMvX12sQ3yz8aDdrwmXdMeEQODPEOw1O+5oQjnYWTkA0KGQ4yWjicWSe64NQXs7xs8M4opBQq41VkSRfkg9U4qQnNHpwdWHaN8VY17laiUXm3a3ZxceC1jD+bFMukyoDB7IfmEhsZlMd/b4tZo/RP65x3wl+2YuTS6AVkZLwRne8b/hAQVJjgeFQFPam3jkgiQryYLIoWP9+rCdPZgeUljMDZgB2Z16zj1FSSG3wNrU/UAwkFg5UXkq3JCfBXk4VGcplh0QLBLSKeVLhiJZIrGVBGDyajpzk2EL0+Hf2AsuhdxF13SJCu3+fbBs+hjpmkbBnTyuWxWrAfdx+ouQrxokRxMSoDxlnCMUKqJKKBrNo+Kau4/DlfauL/qnThtrM6PTeAtE5samA+hRUJGdAj6TVsU/k4CjIZDsi4lU9Nm3g850H6VdWQJTiE4B+D9qXKNnkbNmrOKkBj/5DiNNIxKUtkiIsnVZYOTtqj9oc6LpVBn51y8m7uXUDxbY="
            )
    );

    /**
     * Skin data for header/title tab list slots.
     */
    public static final List<PlayerInfoUpdatePacket.Property> HEADER_SLOT = List.of(
            new PlayerInfoUpdatePacket.Property("textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTY4ODkzOTQyOTIxNSwKICAicHJvZmlsZUlkIiA6ICJlYjA3ZmQzMmFiOTE0NjRjODVjYmU1YjVhYTlkYTRjZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ4bUUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmMzZTE3N2UyODQyYWYyZDIzMDUxYTc0MjMwNWUwYzllM2M4NmY3YWU3MDljOGJlMDVkNGU3MTM1YjM1YTIzOSIKICAgIH0KICB9Cn0=",
                    "xzHcxA9zNPZwFinFhvn3y42AYZJY8PCzUX9XiB/uuEXS31yIZ5SD09EKGgCQHVEva2BfurnnKW6mGt1rBzgMLuHUV7EYtPJ60FilyZs4gziRgNxHDlNDnvqIpJHlHPEaeVUQ9NNXGn5u0Y6vLfIouWA0Rxs6PqdPPzPk4e8tiVsjQYJ3KJoqgWBgCrJ3XIIsA+SfuiML+tg0O/gzraVvsxY1mCtP/gKVK0x6NIpXwR97tYUzX3PCruP3EfdHC9KBeYHYvUuXB54FnXaxtb1t84fInUy6KsuPqy5NYpX/Kcdq5IoBF5tHrzUfoteHubm9GDB6TJ+NKvaLmsCKpNi5706yar13q56CW7uTseFyq35bQ6zERmCKQLHAPI0nnr4wJWkDv81wQ3kvL+kWCwZ5CPqbUHtXqvLo8P76HcAzTfMJYbGvXRK3FtTE7epnK6p+xWV8ahX0ct8oy3IW6M5E33IvPzw7l2KuqIYIqHb9xON2iDMF0DRtbtkgLqegsA/YHe7jMkejIsxah443yW3Ht2PRDqkWSIb78H+GD41J1HJUrk7FLkUnh3SVHKtW96GPzoFY87Zrq1XpmZ3hybaFcFiajBBU6Hha6a0fkE+b2bukli1AWtthDumN79bhPz4ErHm3TyzZ7G2wefGDh2S+RBuaN4NPPwN3UwrwSP9O2bg="
            )
    );
}