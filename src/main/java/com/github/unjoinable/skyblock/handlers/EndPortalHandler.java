package com.github.unjoinable.skyblock.handlers;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class EndPortalHandler implements BlockHandler {
    public static final NamespaceID KEY = NamespaceID.from("minecraft:end_portal");

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from(KEY);
    }

}
