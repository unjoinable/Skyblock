package io.github.unjoinable.skyblock.collections;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.tag.Tag;

import java.util.Collection;

public abstract class SkyblockCollection {
    public static final Tag<SkyblockPlayer> DROPPED_BY_PLAYER = Tag.Transient("dropped_by_player");


}
