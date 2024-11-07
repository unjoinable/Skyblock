package io.github.unjoinable.skyblock;

import io.github.unjoinable.skyblock.commands.ItemCommand;
import io.github.unjoinable.skyblock.commands.NBTCommand;
import io.github.unjoinable.skyblock.handlers.SkullHandler;
import io.github.unjoinable.skyblock.listeners.AsyncPlayerConfigurationListener;
import io.github.unjoinable.skyblock.listeners.ItemDropListener;
import io.github.unjoinable.skyblock.listeners.PickUpItemListener;
import io.github.unjoinable.skyblock.listeners.PlayerSpawnListener;
import io.github.unjoinable.skyblock.registry.registries.ItemRegistry;
import io.github.unjoinable.skyblock.time.SkyblockStandardTime;
import io.github.unjoinable.skyblock.user.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Skyblock {
    private static final Logger logger = LoggerFactory.getLogger(Skyblock.class);
    private static final Pos HUB_SPAWN_POINT = new Pos(-2, 71, -68).withYaw(-180F);

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        new SkyblockStandardTime();
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

        //block handlers
        BlockManager blockManager = MinecraftServer.getBlockManager();
        blockManager.registerHandler(SkullHandler.KEY, SkullHandler::new);

        //let the show begin.
        server.start("0.0.0.0", 25565);

        //after show snacks
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            for (Player serverPlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!serverPlayer.isPlayer()) continue;
                SkyblockPlayer player = ((SkyblockPlayer) serverPlayer);
                //player.tick();
            }
        }, TaskSchedule.seconds(2L), TaskSchedule.immediate());
    }
}