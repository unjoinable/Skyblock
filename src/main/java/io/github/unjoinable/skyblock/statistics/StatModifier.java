package io.github.unjoinable.skyblock.statistics;

/**
 * @param type The value type we want to modify i.e., BASE, ADDITIVE OR MULTIPLICATIVE.
 * @param value The value that modifies the stat
 */
public record StatModifier (StatValueType type, int value) {}
