package com.github.unjoinable.skyblock.item.component;

import com.github.unjoinable.skyblock.item.SkyblockItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface LoreableComponent extends Component {

    int priority();

    @NotNull List<net.kyori.adventure.text.Component>lore(SkyblockItem item);

}
