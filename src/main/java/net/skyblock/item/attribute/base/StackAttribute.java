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
 * Applies this attribute's effects directly to the provided {@link ItemStack.Builder} during item construction.
 *
 * This method enables advanced or non-standard modifications to the item builder, bypassing normal attribute serialization.
 * It is intended for internal use cases where attributes must alter the builder in ways not possible through standard NBT or serialization mechanisms.
 *
 * @param builder   the {@link ItemStack.Builder} instance to modify
 * @param container the {@link AttributeContainer} holding all attributes for potential cross-attribute access
 */
    void applyToBuilder(@NotNull ItemStack.Builder builder, @NotNull AttributeContainer container);
}