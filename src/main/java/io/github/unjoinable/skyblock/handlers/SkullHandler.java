package io.github.unjoinable.skyblock.handlers;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class SkullHandler implements BlockHandler {

    public static final NamespaceID KEY = NamespaceID.from("minecraft:skull");

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from(KEY);
    }

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        // The profile field will hold the NBT contents of ItemComponent.PROFILE
        Tag<?> profileTag = Tag.NBT("profile");

        return Collections.singletonList(profileTag);
    }
}
