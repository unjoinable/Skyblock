package net.unjoinable.utility;

import net.minestom.server.codec.Codec;
import net.minestom.server.codec.StructCodec;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * Record representing a namespaced identifier with a namespace and a key.
 */
public record NamespaceId(@NotNull String namespace, @NotNull String key) {
    public static final Codec<NamespaceId> CODEC = StructCodec.struct(
            "namespace", Codec.STRING, NamespaceId::namespace,
            "key", Codec.STRING, NamespaceId::key,
            NamespaceId::new
    );

    /**
     * Constant for the "skyblock" namespace.
     */
    private static final String SKYBLOCK_NAMESPACE = "skyblock";

    /**
     * Constructor with validation.
     */
    public NamespaceId {
        Objects.requireNonNull(namespace, "Namespace cannot be null");
        Objects.requireNonNull(key, "Key cannot be null");
    }

    /**
     * Creates a new NamespaceId with the "skyblock" namespace.
     *
     * @param key The key for the identifier
     * @return A new NamespaceId with skyblock namespace
     */
    public static NamespaceId fromSkyblock(@NotNull String key) {
        return new NamespaceId(SKYBLOCK_NAMESPACE, key);
    }

    /**
     * Creates a NamespaceId from a string in the format "namespace:key".
     *
     * @param string The string to parse
     * @return The parsed NamespaceId
     * @throws IllegalArgumentException If the string is not in the correct format
     */
    public static NamespaceId fromString(@NotNull String string) {
        Objects.requireNonNull(string, "String cannot be null");

        int separatorIndex = string.indexOf(':');
        if (separatorIndex == -1) {
            throw new IllegalArgumentException("Invalid format, expected 'namespace:key' but got: " + string);
        }

        String namespace = string.substring(0, separatorIndex);
        String key = string.substring(separatorIndex + 1);

        if (namespace.isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be empty");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }

        return new NamespaceId(namespace, key);
    }

    /**
     * Returns a string representation in the format "namespace:key".
     *
     * @return The string representation
     */
    @Override
    public @NotNull String toString() {
        return namespace + ":" + key;
    }

    /**
     * Compares this NamespaceId to another object for equality.
     * Two NamespaceIds are equal if their namespace and key are equal.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespaceId that = (NamespaceId) o;
        return namespace.equals(that.namespace) && key.equals(that.key);
    }

    /**
     * Returns a hash code for this NamespaceId.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }
}