package net.unjoinable.skyblock.level;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;

import java.util.List;

/**
 * Represents a skyblock island with unique identity, spawn location, and regions.
 * Implements {@link Keyed} for namespace-based identification.
 */
public interface Island extends Keyed {

    /**
     * Returns the human-readable display name for UI presentation.
     * 
     * @return the island's display name, never null or empty
     */
    String displayName();

    /**
     * Returns the unique namespaced identifier for this island.
     * 
     * @return the unique key identifier, never null
     */
    Key key();

    /**
     * Returns the file system path to this island's world data.
     * 
     * @return the path to the island's world data, never null or empty
     */
    String worldPath();

    /**
     * Returns the default spawn position for players joining this island.
     * 
     * @return the spawn position with coordinates and orientation, never null
     */
    Pos spawnPoint();

    /**
     * Returns all regions defined within this island.
     * 
     * @return an immutable list of regions, never null but may be empty
     */
    List<Region> regions();

    /**
     * Finds the first region containing the specified point.
     *
     * @param point the 3D coordinate to search for
     * @return the first matching region, or Region.NONE if none found
     */
    default Region getRegion(Point point) {
        return regions()
                .stream()
                .filter(region -> region.contains(point))
                .findFirst().orElse(Region.NONE);
    }
}