package net.unjoinable.skyblock.level;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.unjoinable.skyblock.utils.MiniString;

/**
 * Represents a spatial region within the skyblock game world for collision detection
 * and spatial queries. Implementations define bounded 3D areas like spawn zones or
 * protected areas.
 */
public interface Region {

    /**
     * Empty region that contains no points. Used as null object pattern.
     */
    Region NONE = new NoneRegion();

    /**
     * Tests if the specified point lies within this region's boundaries.
     * Points on boundaries are considered inside.
     *
     * @param point the 3D coordinate to test
     * @return true if point is within or on boundary, false otherwise
     */
    boolean contains(Point point);

    /**
     * Returns the displayable name of this region.
     *
     * @return the region's name
     */
    Component displayName();

    // Factory methods

    /**
     * Creates a rectangular cuboid region from two opposite corners.
     */
    static Region cuboid(Component name, Point corner1, Point corner2) {
        return new CuboidRegion(name, corner1, corner2);
    }

    /**
     * Creates a cube region from center point and half-size.
     */
    static Region cube(Component name, Point center, double halfSize) {
        return new CubeRegion(name, center, halfSize);
    }

    /**
     * Creates a spherical region from center point and radius.
     */
    static Region sphere(Component name, Point center, double radius) {
        return new SphereRegion(name, center, radius);
    }

    /**
     * Creates a cylindrical region from center point, radius, and height.
     */
    static Region cylinder(Component name, Point center, double radius, double height) {
        return new CylinderRegion(name, center, radius, height);
    }
}

/**
 * Rectangular cuboid region implementation.
 */
record CuboidRegion(
        Component displayName,
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ) implements Region {

    CuboidRegion(Component name, Point corner1, Point corner2) {
        this(name,
                Math.min(corner1.x(), corner2.x()),
                Math.min(corner1.y(), corner2.y()),
                Math.min(corner1.z(), corner2.z()),
                Math.max(corner1.x(), corner2.x()),
                Math.max(corner1.y(), corner2.y()),
                Math.max(corner1.z(), corner2.z())
        );
    }

    @Override
    public boolean contains(Point point) {
        return point.x() >= minX && point.x() <= maxX &&
                point.y() >= minY && point.y() <= maxY &&
                point.z() >= minZ && point.z() <= maxZ;
    }
}

/**
 * Cube region implementation using center point and half-size.
 */
record CubeRegion(
        Component displayName,
        double centerX,
        double centerY,
        double centerZ,
        double halfSize) implements Region {

    CubeRegion(Component name, Point center, double halfSize) {
        this(name, center.x(), center.y(), center.z(), halfSize);
    }

    @Override
    public boolean contains(Point point) {
        return Math.abs(point.x() - centerX) <= halfSize &&
                Math.abs(point.y() - centerY) <= halfSize &&
                Math.abs(point.z() - centerZ) <= halfSize;
    }
}

/**
 * Spherical region implementation using center point and radius.
 */
record SphereRegion(
        Component displayName,
        double centerX,
        double centerY,
        double centerZ,
        double radiusSquared) implements Region {

    SphereRegion(Component name, Point center, double radius) {
        this(name, center.x(), center.y(), center.z(), radius * radius);
    }

    @Override
    public boolean contains(Point point) {
        double dx = point.x() - centerX;
        double dy = point.y() - centerY;
        double dz = point.z() - centerZ;
        return (dx * dx + dy * dy + dz * dz) <= radiusSquared;
    }
}

/**
 * Cylindrical region implementation with circular base and height.
 */
record CylinderRegion(
        Component displayName,
        double centerX,
        double centerZ,
        double baseY,
        double radiusSquared,
        double height) implements Region {

    CylinderRegion(Component name, Point center, double radius, double height) {
        this(name, center.x(), center.z(), center.y(), radius * radius, height);
    }

    @Override
    public boolean contains(Point point) {
        if (point.y() < baseY || point.y() > baseY + height) {
            return false;
        }

        double dx = point.x() - centerX;
        double dz = point.z() - centerZ;
        return (dx * dx + dz * dz) <= radiusSquared;
    }
}

/**
 * Empty region implementation that contains no points.
 */
record NoneRegion() implements Region {
    private static final Component NONE_NAME = MiniString.asComponent("<gray>None");

    @Override
    public boolean contains(Point point) {
        return false;
    }

    @Override
    public Component displayName() {
        return NONE_NAME;
    }
}