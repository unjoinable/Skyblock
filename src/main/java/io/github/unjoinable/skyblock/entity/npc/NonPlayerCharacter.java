package io.github.unjoinable.skyblock.entity.npc;

import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import io.github.unjoinable.skyblock.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class NonPlayerCharacter extends LivingEntity {
    private static final Component NPC_MSG = StringUtils.toComponent("<yellow>[NPC] ");

    public NonPlayerCharacter() {
        super(EntityType.PLAYER);
    }

    public abstract @NotNull String getName();

    public void sendMessage(@NotNull SkyblockPlayer player, @NotNull Component message) {
        player.sendMessage(NPC_MSG.append(message));
    }
}
