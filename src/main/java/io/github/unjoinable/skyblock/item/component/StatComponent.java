package io.github.unjoinable.skyblock.item.component;

import io.github.unjoinable.skyblock.statistics.StatModifier;
import io.github.unjoinable.skyblock.statistics.Statistic;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface StatComponent extends Component {

    @NotNull Map<Statistic, List<StatModifier>> statModifiers();

}
