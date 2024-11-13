package io.github.unjoinable.skyblock.handlers;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class EnchantingTableHandler implements BlockHandler {

    public static final NamespaceID KEY = NamespaceID.from("minecraft:enchanting_table");

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from(KEY);
    }
}
