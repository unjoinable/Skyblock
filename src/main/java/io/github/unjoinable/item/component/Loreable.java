package io.github.unjoinable.item.component;

import io.github.unjoinable.item.SkyblockItem;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Loreable {

    int priority();

    @NotNull List<Component> lore(@NotNull SkyblockItem item);

}
