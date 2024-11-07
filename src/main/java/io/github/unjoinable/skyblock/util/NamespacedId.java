package io.github.unjoinable.skyblock.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a unique identifier consisting of a namespace and a key.
 *
 * @param namespace The namespace of the identifier.
 * @param key The key of the identifier.
 */
public record NamespacedId(String namespace, String key) {
    public static final NamespacedId AIR = NamespacedId.fromSkyblockNamespace("air");
    private static final String SKYBLOCK = "skyblock";

    /**
     * Returns a string representation of this NamespacedId in the format "namespace:key".
     *
     * @return The string representation of this NamespacedId.
     */
    @Override
    public String toString() {
        return namespace + ":" + key;
    }

    /**
     * Creates a new NamespacedId from a string in the format "namespace:key".
     *
     * @param string The string to parse.
     * @return The parsed NamespacedId.
     * @throws IllegalArgumentException If the string is not in the correct format.
     */
    public static @NotNull NamespacedId fromString(@NotNull String string) {
        String[] split = string.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid NamespacedId: " + string);
        }
        return new NamespacedId(split[0], split[1]);
    }

    /**
     * Creates a new NamespacedId with the given key and the "skyblock" namespace.
     *
     * @param key The key of the identifier.
     * @return The created NamespacedId.
     */
    public static @NotNull NamespacedId fromSkyblockNamespace(@NotNull String key) {
        return new NamespacedId(SKYBLOCK, key.toLowerCase());
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * @param o The namespace to compare with.
     * @return true if the objects are the same; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespacedId that = (NamespacedId) o;
        return Objects.equals(namespace, that.namespace) && Objects.equals(key, that.key);
    }
}
