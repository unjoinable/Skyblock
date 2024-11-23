package com.github.unjoinable.skyblock.entity;

import com.github.unjoinable.skyblock.entity.mobs.Zombie;

import java.util.function.Supplier;

public enum SkyblockMob {
    ZOMBIE(Zombie::new),
    ;

    private final Supplier<SkyblockEntity> supplier;

    SkyblockMob(Supplier<SkyblockEntity> supplier) {
        this.supplier = supplier;
    }

    public SkyblockEntity get() {
        return supplier.get();
    }
}
