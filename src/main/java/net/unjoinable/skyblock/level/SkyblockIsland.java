package net.unjoinable.skyblock.level;

import net.kyori.adventure.key.Key;
import net.minestom.server.coordinate.Pos;

import java.util.List;

/**
 * Predefined skyblock islands with their spawn locations and world paths.
 */
public enum SkyblockIsland implements Island {

    HUB(
            "Hub",
            Key.key("skyblock:hub"),
            "worlds/hub",
            new Pos(-2, 71, -68, 180F, 0f),
            List.of()
    ),
    
    CRIMSON_ISLE(
            "Crimson Isle",
            Key.key("skyblock:crimson_isle"),
            "worlds/crimson_isle",
            new Pos(-360.5, 80, -430.5, -180f, 0f),
            List.of()
    ),
    
    DEEP_CAVERNS(
            "Deep Caverns",
            Key.key("skyblock:deep"),
            "worlds/deep",
            new Pos(4, 157, 85, 180, 0),
            List.of()
    ),
    
    END(
            "The End",
            Key.key("skyblock:end"),
            "worlds/end",
            new Pos(-503, 101, -275, 90f, 0f),
            List.of()
    ),
    
    FARMING_ISLES(
            "Farming Islands",
            Key.key("skyblock:farming_isles"),
            "worlds/farming_isles",
            new Pos(113.5, 71, -207.5, -135f, 0f),
            List.of()
    ),
    
    GOLD_MINE(
            "Gold Mine",
            Key.key("skyblock:gold"),
            "worlds/gold",
            new Pos(-4.5, 74, -278, -180F, 0),
            List.of()
    ),
    
    PARK(
            "Park",
            Key.key("skyblock:park"),
            "worlds/park",
            new Pos(0, 64, 0),
            List.of()
    ),
    
    SPIDERS_DEN(
            "Spider's Den",
            Key.key("skyblock:spiders_den"),
            "worlds/spiders_den",
            new Pos(-202.5, 83, -233.5, 100, 0),
            List.of()
    );

    private final Entry island;

    /**
     * Creates an island enum constant.
     */
    SkyblockIsland(String displayName, Key key, String worldPath, Pos spawnPoint, List<Region> regions) {
        this.island = new Entry(displayName, key, worldPath, spawnPoint, regions);
    }

    @Override
    public String displayName() {
        return island.displayName();
    }

    @Override
    public Key key() {
        return island.key();
    }

    @Override
    public String worldPath() {
        return island.worldPath();
    }

    @Override
    public Pos spawnPoint() {
        return island.spawnPoint();
    }

    @Override
    public List<Region> regions() {
        return island.regions();
    }

    /**
     * Internal island data record.
     */
    private record Entry(
            String displayName,
            Key key,
            String worldPath,
            Pos spawnPoint,
            List<Region> regions) implements Island {}
}
