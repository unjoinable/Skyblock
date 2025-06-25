package net.unjoinable.skyblock.data;

import net.unjoinable.skyblock.utility.NamespaceId;
import org.jetbrains.annotations.NotNull;

/**
 * A generic and type-safe implementation of the {@link DataKey} interface.
 * <p>
 * This implementation allows associating a {@link NamespaceId} with a specific data type,
 * enabling safe and centralized access to strongly typed data keys.
 *
 * <p>Example usage:
 * <pre>{@code
 * DataKey<Long> miningXP = DataKeyImpl.of("skill:mining", Long.class);
 * }</pre>
 *
 * @param id   the {@link NamespaceId} that uniquely identifies the data key
 * @param type the class type associated with the data key (e.g. Long.class)
 * @param <T>  the type of data this key holds
 */
record DataKeyImpl<T>(
        @NotNull NamespaceId id,
        @NotNull Class<T> type) implements DataKey<T> {

    /**
     * Creates a new {@code DataKeyImpl} instance from a namespaced ID string.
     *
     * @param idString the string representation of the {@link NamespaceId} (e.g., "skill:mining")
     * @param type     the class type associated with this data key
     * @param <T>      the type of the data
     * @return a new {@code DataKeyImpl<T>} instance
     */
    public static <T> @NotNull DataKeyImpl<T> of(@NotNull String idString, @NotNull Class<T> type) {
        return new DataKeyImpl<>(NamespaceId.fromString(idString), type);
    }

    /**
     * Creates a new {@code DataKeyImpl} instance from a {@link NamespaceId}.
     *
     * @param id   the {@link NamespaceId}
     * @param type the class type associated with this data key
     * @param <T>  the type of the data
     * @return a new {@code DataKeyImpl<T>} instance
     */
    public static <T> @NotNull DataKeyImpl<T> of(@NotNull NamespaceId id, @NotNull Class<T> type) {
        return new DataKeyImpl<>(id, type);
    }
}
