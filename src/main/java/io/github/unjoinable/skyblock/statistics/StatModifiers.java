package io.github.unjoinable.skyblock.statistics;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StatModifiers {
    private final List<StatModifier> modifiers;
    private double effectiveValue;

    public StatModifiers(@NotNull List<StatModifier> modifiers) {
        this.modifiers = modifiers;
        calculateEffectiveValue();
    }

    public StatModifiers() {
        this.modifiers = new ArrayList<>();
    }

    private void calculateEffectiveValue() {
        var base = 0;
        var multiply = 1;
        var additive = 0;

        for (StatModifier modifier : modifiers) {
            switch (modifier.type()) {
                case BASE -> base += modifier.value();
                case MULTIPLICATIVE -> multiply *= modifier.value();
                case ADDITIVE -> additive += modifier.value();
            }

        }

        effectiveValue = base * (1 + additive) * multiply;
    }

    public void addModifier(@NotNull StatModifier modifier) {
        modifiers.add(modifier);
        calculateEffectiveValue();
    }

    public void removeModifier(@NotNull StatModifier modifier) {
        modifiers.remove(modifier);
        calculateEffectiveValue();
    }

    public void removeModifiers(@NotNull Collection<StatModifier> modifiers) {
        this.modifiers.removeAll(modifiers);
        calculateEffectiveValue();
    }

    public void addModifiers(@NotNull Collection<StatModifier> modifiers) {
        this.modifiers.addAll(modifiers);
        calculateEffectiveValue();
    }

    public double getEffectiveValue() {
        return effectiveValue;
    }
}
