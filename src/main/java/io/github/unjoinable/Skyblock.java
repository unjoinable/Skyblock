package io.github.unjoinable;

import io.github.unjoinable.commands.ItemCommand;
import io.github.unjoinable.commands.NBTCommand;
import io.github.unjoinable.item.SkyblockItemProcessor;
import io.github.unjoinable.listeners.AsyncPlayerConfigurationListener;
import io.github.unjoinable.listeners.ItemDropListener;
import io.github.unjoinable.listeners.PickUpItemListener;
import io.github.unjoinable.listeners.PlayerSpawnListener;
import io.github.unjoinable.user.SkyblockPlayer;
import io.github.unjoinable.registry.registries.ItemRegistry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.timer.TaskSchedule;

public class Skyblock {
    private static final Pos HUB_SPAWN_POINT = new Pos(-2, 71, -68).withYaw(-180F);
    private static SkyblockItemProcessor itemProcessor;

    public static void main(String[] args) {
        itemProcessor = new SkyblockItemProcessor();
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        ItemRegistry.getInstance().registerAll();

        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer hubInstance = instanceManager.createInstanceContainer();
        hubInstance.setChunkLoader(new AnvilLoader("hub"));

        //listeners
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(new ItemDropListener());
        globalEventHandler.addListener(new AsyncPlayerConfigurationListener(hubInstance, HUB_SPAWN_POINT));
        globalEventHandler.addListener(new PickUpItemListener());
        globalEventHandler.addListener(new PlayerSpawnListener());

        //commands
        CommandManager manager =  MinecraftServer.getCommandManager();
        manager.register(new ItemCommand("item"));
        manager.register(new NBTCommand("nbt"));

        //let the show begin.
        server.start("0.0.0.0", 25565);

        //after show snacks
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            for (Player serverPlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!serverPlayer.isPlayer()) continue;
                SkyblockPlayer player = ((SkyblockPlayer) serverPlayer);
                player.tick();
            }
        }, TaskSchedule.seconds(2L), TaskSchedule.immediate());
        //nom nom nom yummy this is probably for ticking scoreboards and action aka life of the da game baby.
    }

    public static SkyblockItemProcessor getItemProcessor() {
        return itemProcessor;
    }
}