package com.github.unjoinable.skyblock.item.component.trait;

import com.github.unjoinable.skyblock.item.component.Component;

/**
 * Interface for components that can be deserialized from persistent storage.
 * <p>
 * Implementing classes should provide a static read method with the signature:
 * <pre>
 * public static @NotNull Optional<? extends DeserializableComponent> read(@NotNull ItemStack itemStack)
 * </pre>
 */
public interface DeserializableComponent extends Component {
    // No methods - this is a marker interface with documentation
}