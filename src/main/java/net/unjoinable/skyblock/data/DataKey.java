package net.unjoinable.skyblock.data;

import net.unjoinable.skyblock.utils.NamespaceId;

public interface DataKey<T> {

    NamespaceId id();

    Class<T> type();

}
