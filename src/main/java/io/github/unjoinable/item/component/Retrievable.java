package io.github.unjoinable.item.component;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Retrievable {

    @Nullable ItemComponent retrieve(@NotNull ItemStack item);

}
