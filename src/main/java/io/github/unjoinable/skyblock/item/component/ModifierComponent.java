package io.github.unjoinable.skyblock.item.component;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ModifierComponent extends BasicComponent {

    @NotNull Component retrieve(ItemStack.@NotNull Builder builder);

}
