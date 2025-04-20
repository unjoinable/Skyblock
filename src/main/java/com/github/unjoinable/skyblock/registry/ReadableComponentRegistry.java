package com.github.unjoinable.skyblock.registry;

import com.github.unjoinable.skyblock.item.component.components.RarityComponent;
import com.github.unjoinable.skyblock.item.component.trait.NBTReadable;
import com.github.unjoinable.skyblock.item.enums.Rarity;

public final class ReadableComponentRegistry extends Registry<Class<? extends NBTReadable>, NBTReadable> {

    @Override
    public void init() {
        register(new RarityComponent(Rarity.UNOBTAINABLE, false));
        lock();
    }

    private void register(NBTReadable readable) {
        register(readable.getClass(), readable);
    }
}