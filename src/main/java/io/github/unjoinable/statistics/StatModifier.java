package io.github.unjoinable.statistics;

/**
 * @param type The value type we want to modify i.e., BASE, ADDITIVE OR MULTIPLICATIVE.
 * @param value The value that modifies the stat
 * @since 1.0.0
 */
public record StatModifier (StatValueType type, int value) {}
