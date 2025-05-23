package net.unjoinable;

import net.minestom.server.MinecraftServer;
import net.minestom.server.codec.Codec;
import net.minestom.server.extras.MojangAuth;
import net.unjoinable.item.SkyblockItem;
import net.unjoinable.item.attribute.ItemAttribute;
import net.unjoinable.item.service.ItemProcessor;
import net.unjoinable.player.factory.PlayerFactory;
import net.unjoinable.registry.Registry;
import net.unjoinable.registry.factory.RegistryFactory;
import net.unjoinable.utility.NamespaceId;

public class Skyblock {
    private final Registry<NamespaceId, SkyblockItem> itemRegistry;
    private final Registry<Class<? extends ItemAttribute>, Codec<? extends ItemAttribute>> attributeCodecRegistry;

    private final ItemProcessor itemProcessor;

    private Skyblock() {
        // Registries
        this.itemRegistry = RegistryFactory.createItemRegistry();
        this.attributeCodecRegistry = RegistryFactory.createAttributeCodecRegistry();

        // Systems
        this.itemProcessor = new ItemProcessor(this.attributeCodecRegistry, this.itemRegistry);

        // Server
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(new PlayerFactory(itemProcessor));

        server.start("0.0.0.0", 25565);
    }

    public static void main(String[] args) {
        Skyblock skyblock = new Skyblock();
    }
}
