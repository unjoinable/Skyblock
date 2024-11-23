package com.github.unjoinable.skyblock.util;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object with a namespaced identifier.
 */
public interface NamespacedObject {
    
    /**
     * Retrieves the namespaced identifier of this object.
     *
     * @return The NamespacedId associated with this object.
     */
    @NotNull NamespacedId id();
    
}
