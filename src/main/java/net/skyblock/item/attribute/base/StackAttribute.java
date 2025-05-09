package net.skyblock.item.attribute.base;

import net.minestom.server.item.ItemStack;
import net.skyblock.item.attribute.AttributeContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for attributes that directly modify an {@link ItemStack.Builder}.
 * <p>
 * <strong>INTERNAL IMPLEMENTATION:</strong> This interface is intended for advanced
 * internal system use only. It provides direct access to modify the item's builder
 * during construction, which bypasses the normal attribute serialization process.
 * </p>
 * <p>
 * This interface should be used sparingly and only when necessary, such as:
 * <ul>
 *   <li>When an attribute needs to modify non-NBT aspects of an item</li>
 *   <li>When complex modifications cannot be easily represented via standard NBT</li>
 *   <li>When performance-critical operations need to avoid serialization overhead</li>
 *   <li>For system-level attributes that handle core item functionality</li>
 * </ul>
 * </p>
 * <p>
 * Due to its low-level nature, this interface may break encapsulation and
 * should be implemented with caution.
 * </p>
 */
public interface StackAttribute extends ItemAttribute {

    /**
     * Applies this attribute's modifications directly to the given item builder.
     * This method is called during item construction to allow attributes to
     * make direct modifications to the builder before the final item is created.
     *
     * @param builder   The item builder to modify
     * @param container The container containing all attributes, which might be used to get other attributes
     */
    void applyToBuilder(@NotNull ItemStack.Builder builder, @NotNull AttributeContainer container);
}