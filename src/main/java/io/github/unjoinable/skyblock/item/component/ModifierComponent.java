package io.github.unjoinable.skyblock.item.component;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModifierComponent extends BasicComponent {

    @Nullable Component retrieve(ItemStack.@NotNull Builder builder);

}
