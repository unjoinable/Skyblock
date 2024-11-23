package io.github.unjoinable.skyblock.statistics.holders;

/**
 * @param type The value type we want to modify i.e., BASE, BONUS, ADDITIVE OR MULTIPLICATIVE.
 * @param value The value that modifies the stat
 */
public record StatModifier (StatValueType type, int value) {}
