package io.github.unjoinable.skyblock.item.component;

import io.github.unjoinable.skyblock.statistics.holders.StatModifiersMap;
import org.jetbrains.annotations.NotNull;

public interface StatComponent extends Component {

    @NotNull StatModifiersMap statModifiers(@NotNull ComponentContainer container);

}
