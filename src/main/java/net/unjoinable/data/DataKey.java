package net.unjoinable.data;

import net.unjoinable.utility.NamespaceId;
import org.jetbrains.annotations.NotNull;

public interface DataKey<T> {

    @NotNull NamespaceId id();

    @NotNull Class<T> type();

}
