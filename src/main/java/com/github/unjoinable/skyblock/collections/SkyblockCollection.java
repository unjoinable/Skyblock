package com.github.unjoinable.skyblock.collections;

import com.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.tag.Tag;

public abstract class SkyblockCollection {
    public static final Tag<SkyblockPlayer> DROPPED_BY_PLAYER = Tag.Transient("dropped_by_player");
}
