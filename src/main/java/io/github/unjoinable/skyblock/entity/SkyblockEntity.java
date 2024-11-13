package io.github.unjoinable.skyblock.entity;

import org.jetbrains.annotations.NotNull;

public interface SkyblockEntity {

    @NotNull String name();

    int getHealth(int lvl);

    int getDefense(int lvl);

    int getDamage(int lvl);

}
