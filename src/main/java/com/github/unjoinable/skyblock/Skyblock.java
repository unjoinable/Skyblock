package com.github.unjoinable.skyblock;

import com.github.unjoinable.skyblock.commands.*;
import com.github.unjoinable.skyblock.handlers.*;
import com.github.unjoinable.skyblock.listeners.*;
import com.github.unjoinable.skyblock.island.Island;
import com.github.unjoinable.skyblock.registry.registries.AbilityRegistry;
import com.github.unjoinable.skyblock.registry.registries.ClickableItemRegistry;
import com.github.unjoinable.skyblock.registry.registries.ItemRegistry;
import com.github.unjoinable.skyblock.time.SkyblockStandardTime;
import com.github.unjoinable.skyblock.user.SkyblockPlayer;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Skyblock {
    private static InstanceContainer hubInstance;
    private static final Logger logger = LoggerFactory.getLogger(Skyblock.class);
    private static SkyblockStandardTime skyblockStandardTime;

    public static void main(String[] args) {
        System.setProperty("minestom.tps", "60");
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        skyblockStandardTime = new SkyblockStandardTime();

        //registries
        AbilityRegistry.getInstance().registerAll();
        ItemRegistry.getInstance().registerAll();
        ClickableItemRegistry.getInstance().registerAll();

        //Setting Player Provider
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        //Instance loading
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        hubInstance = instanceManager.createInstanceContainer();
        hubInstance.setChunkLoader(new AnvilLoader("hub"));

        //listeners
        registerAllEvents();

        //commands
        registerAllCommands();

        //block handlers
        registerAllBlockHandlers();


        //let the show begin.
        server.start("0.0.0.0", 25565);

        MinecraftServer.getSchedulerManager().submitTask(() -> {
            for (Player serverPlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!serverPlayer.isPlayer()) continue;
                SkyblockPlayer player = ((SkyblockPlayer) serverPlayer);
                player.taskLoop();
            }
            return TaskSchedule.seconds(1L);
        }, ExecutionType.TICK_END);
    }

    //register
    private static void registerAllEvents() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(new ItemDropListener());
        globalEventHandler.addListener(new AsyncPlayerConfigurationListener(hubInstance, Island.HUB.spawn()));
        globalEventHandler.addListener(new PickUpItemListener());
        globalEventHandler.addListener(new PlayerSpawnListener());
        globalEventHandler.addListener(new PlayerUseItemListener());
        globalEventHandler.addListener(new SkyblockAbilityUseListener());
        globalEventHandler.addListener(new InventoryPreClickListener());
        globalEventHandler.addListener(new EntityAttackListener());
        globalEventHandler.addListener(new SkyblockStatUpdateListener());
    }

    private static void registerAllCommands() {
        CommandManager manager =  MinecraftServer.getCommandManager();
        manager.register(new ItemCommand());
        manager.register(new ServerResourcesCommand());
        manager.register(new TestCommand());
        manager.register(new SpawnCustomMobCommand());
        manager.register(new MenuCommand());
    }

    private static void registerAllBlockHandlers() {
        BlockManager blockManager = MinecraftServer.getBlockManager();
        blockManager.registerHandler(SkullHandler.KEY, SkullHandler::new);
        blockManager.registerHandler(SignHandler.KEY, SkullHandler::new);
        blockManager.registerHandler(JukeBoxHandler.KEY, JukeBoxHandler::new);
        blockManager.registerHandler(HopperHandler.KEY, HopperHandler::new);
        blockManager.registerHandler(FurnaceHandler.KEY, FurnaceHandler::new);
        blockManager.registerHandler(EndPortalHandler.KEY, EndPortalHandler::new);
        blockManager.registerHandler(EnchantingTableHandler.KEY, EnchantingTableHandler::new);
        blockManager.registerHandler(DropperHandler.KEY, DropperHandler::new);
        blockManager.registerHandler(DispenserHandler.KEY, DispenserHandler::new);
        blockManager.registerHandler(ComparatorHandler.KEY, ComparatorHandler::new);
        blockManager.registerHandler(ChestHandler.KEY, ChestHandler::new);
        blockManager.registerHandler(BrewingStandHandler.KEY, BrewingStandHandler::new);
        blockManager.registerHandler(BedHandler.KEY, BedHandler::new);
        blockManager.registerHandler(BeaconHandler.KEY, BeaconHandler::new);
        blockManager.registerHandler(BannerHandler.KEY, BannerHandler::new);
    }

    //getters
    public static SkyblockStandardTime getSkyblockStandardTime() {
        return skyblockStandardTime;
    }

}