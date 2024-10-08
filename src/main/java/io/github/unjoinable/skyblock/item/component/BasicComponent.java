package io.github.unjoinable.skyblock.item.component;

import io.github.unjoinable.skyblock.item.SkyblockItem;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//only needs to be applied not retrieved
public interface BasicComponent extends Component {

    default void applyData(@NotNull ComponentContainer container) {} //run method during creation of skyblock item

    default void applyEffect(@NotNull SkyblockItem item, @NotNull ItemStack.Builder builder) {} //run during stack creation

}
