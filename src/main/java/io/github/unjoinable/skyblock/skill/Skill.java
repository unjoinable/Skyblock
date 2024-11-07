package io.github.unjoinable.skyblock.skill;

import java.util.Arrays;
import java.util.Collection;

public enum Skill {
    DUNGEONEERING,
    FARMING,
    FORAGING,
    COMBAT,
    RUNE_CRAFTING,
    ;

    private static final Collection<Skill> VALUES =  Arrays.asList(values());

    public static Collection<Skill> getValues() {
        return VALUES;
    }
}
