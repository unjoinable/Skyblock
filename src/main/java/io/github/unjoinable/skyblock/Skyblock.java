package io.github.unjoinable.skyblock;

import io.github.unjoinable.skyblock.commands.*;
import io.github.unjoinable.skyblock.handlers.*;
import io.github.unjoinable.skyblock.listeners.*;
import io.github.unjoinable.skyblock.registry.registries.AbilityRegistry;
import io.github.unjoinable.skyblock.registry.registries.ClickableButtonRegistry;
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
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Skyblock {
    private static final Logger logger = LoggerFactory.getLogger(Skyblock.class);
    private static final Pos HUB_SPAWN_POINT = new Pos(-2, 71, -68).withYaw(-180F);
    private static SkyblockStandardTime skyblockStandardTime;

    public static void main(String[] args) {
        System.setProperty("minestom.tps", "60");
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        skyblockStandardTime = new SkyblockStandardTime();

        AbilityRegistry.getInstance().registerAll();
        ItemRegistry.getInstance().registerAll();
        ClickableButtonRegistry.getInstance().registerAll();

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
        globalEventHandler.addListener(new PlayerUseItemListener());
        globalEventHandler.addListener(new SkyblockAbilityUseListener());
        globalEventHandler.addListener(new InventoryPreClickListener());
        globalEventHandler.addListener(new EntityAttackListener());
        globalEventHandler.addListener(new SkyblockDamageListener());

        //commands
        CommandManager manager =  MinecraftServer.getCommandManager();
        manager.register(new ItemCommand("item"));
        manager.register(new NBTCommand("nbt"));
        manager.register(new ServerResourcesCommand());
        manager.register(new TestCommand());
        manager.register(new SpawnCustomMobCommand());
        manager.register(new MenuCommand());

        //block handlers
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


        //let the show begin.
        server.start("0.0.0.0", 25565);

        //after show snacks
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            for (Player serverPlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!serverPlayer.isPlayer()) continue;
                SkyblockPlayer player = ((SkyblockPlayer) serverPlayer);
                player.taskLoop();
            }
            return TaskSchedule.seconds(1L);
        }, ExecutionType.TICK_END);
    }

    //getters
    public static SkyblockStandardTime getSkyblockStandardTime() {
        return skyblockStandardTime;
    }

}