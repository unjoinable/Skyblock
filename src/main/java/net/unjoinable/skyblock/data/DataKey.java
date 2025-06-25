package net.unjoinable.skyblock.data;

import net.unjoinable.skyblock.utility.NamespaceId;

public interface DataKey<T> {

    NamespaceId id();

    Class<T> type();

}
