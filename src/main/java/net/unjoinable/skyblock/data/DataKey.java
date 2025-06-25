package net.unjoinable.skyblock.data;

import net.unjoinable.skyblock.utility.NamespaceId;
import org.jetbrains.annotations.NotNull;

public interface DataKey<T> {

    @NotNull NamespaceId id();

    @NotNull Class<T> type();

}
