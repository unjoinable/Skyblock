package net.skyblock.item.attribute.base;

/**
 * A marker interface for {@link ItemAttribute} implementations that are specifically
 * designed to be serialized to and deserialized from JSON format.
 * <p>
 * Implementing this interface indicates that the attribute's data will be stored in JSON format
 * when persisting items. The underlying codec from {@link ItemAttribute} will be used
 * for the actual serialization and deserialization operations.
 * </p>
 * <p>
 * This interface does not add any additional methods beyond those inherited from {@link ItemAttribute},
 * but serves as a type indicator for the attribute system to determine the appropriate
 * serialization strategy.
 * </p>
 * <p>
 * Usage example:
 * <pre>
 * public class MyJsonAttribute implements JsonAttribute {
 *     // Implementation of CodecAttribute methods
 *     // JSON-specific implementation details
 * }
 * </pre>
 * </p>
 * <p>
 * JSON attributes are particularly useful for configuration files, API responses, and other
 * human-readable data exchange formats.
 * </p>
 */
public interface JsonAttribute extends ItemAttribute {
    // Marker interface - no additional methods required
}