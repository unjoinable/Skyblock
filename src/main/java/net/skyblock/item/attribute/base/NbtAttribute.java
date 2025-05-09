package net.skyblock.item.attribute.base;

/**
 * A marker interface for {@link ItemAttribute} implementations that are specifically
 * designed to be serialized to and deserialized from NBT (Named Binary Tag) format.
 * <p>
 * Implementing this interface indicates that the attribute's data will be stored in NBT format
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
 * public class MyNbtAttribute implements NbtAttribute {
 *     // Implementation of CodecAttribute methods
 *     // NBT-specific implementation details
 * }
 * </pre>
 * </p>
 */
public interface NbtAttribute extends ItemAttribute {
    // Marker interface - no additional methods required
}