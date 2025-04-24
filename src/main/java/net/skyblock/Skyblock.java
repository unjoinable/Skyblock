package net.skyblock;

import net.skyblock.command.commands.ItemCommand;
import net.skyblock.command.commands.TestCommand;
import net.skyblock.item.SkyblockItemProcessor;
import net.skyblock.item.component.ComponentContainer;
import net.skyblock.item.component.components.ArtOfPeaceComponent;
import net.skyblock.item.component.components.HotPotatoBookComponent;
import net.skyblock.item.component.components.RarityComponent;
import net.skyblock.listeners.StatModifierSyncListener;
import net.skyblock.listeners.AsyncPlayerConfigurationListener;
import net.skyblock.player.SkyblockPlayer;
import net.skyblock.registry.Registry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Skyblock server implementation.
 * This class handles server initialization, registry loading, and world setup.
 * It serves as the entry point for the entire Skyblock server application.
 */
public class Skyblock {
    private static SkyblockItemProcessor processor;
    private static InstanceContainer hubInstance;
    private static final Logger logger = LoggerFactory.getLogger(Skyblock.class);

    /**
     * Main entry point for the Skyblock server.
     * Initializes the server, registers event listeners, loads registries,
     * creates the hub world, and starts the server.
     *
     * @param args Command line arguments (unused)
     */
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        MojangAuth.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyblockPlayer::new);

        // Set up world instances
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        loadHub(instanceManager);

        // Register event listeners before registries to ensure proper registration order
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        registerListeners(eventHandler);

        // Initialize item and component registries
        initRegistries();
        initProcesor();

        // Register commands
        CommandManager cmdManager = MinecraftServer.getCommandManager();
        registerCommands(cmdManager);

        // Start the server on the default Minecraft port
        server.start("0.0.0.0", 25565);
    }

    /**
     * Registers all event listeners required for the Skyblock server.
     */
    private static void registerListeners(GlobalEventHandler eventHandler) {
        eventHandler.addListener(new AsyncPlayerConfigurationListener(hubInstance, new Pos(-2, 71, -68).withYaw(-180F)));
        ComponentContainer.addListener(new StatModifierSyncListener());
    }

    /**
     * Initializes all registries needed for the Skyblock server.
     * This includes the item registry and component registry which handle
     * custom items and their associated components/behaviors.
     */
    private static void initRegistries() {
        //Registry.COMPONENT_REGISTRY.init();
        Registry.ITEM_REGISTRY.init();

    }

    /**
     * Registers all default components to processor
     */
    private static void initProcesor() {
        processor = new SkyblockItemProcessor();
        processor.registerDeserializer(HotPotatoBookComponent::read);
        processor.registerDeserializer(RarityComponent::read);
        processor.registerDeserializer(ArtOfPeaceComponent::read);
    }

    /**
     * Creates and loads the hub world instance that players will initially spawn in.
     * Uses an AnvilLoader to load chunk data from the "hub" directory.
     *
     * @param instanceManager The Minestom instance manager used to create world instances
     */
    private static void loadHub(InstanceManager instanceManager) {
        hubInstance = instanceManager.createInstanceContainer();
        hubInstance.setChunkLoader(new AnvilLoader("hub"));
    }

    /**
     * Registers all the commands
     * @param cmdManager The Minestom command manager used to register commands.
     */
    private static void registerCommands(CommandManager cmdManager) {
        cmdManager.register(new ItemCommand());
        cmdManager.register(new TestCommand());
    }

    /**
     * Returns the server's logger for Skyblock-related logging.
     *
     * @return The SLF4J logger for this class
     */
    public static Logger getLogger() {
        return logger;
    }


    /**
     * The instance of processor with default components registered
     * @return The Skyblock Item Processor
     */
    public static SkyblockItemProcessor getProcessor() {
        return processor;
    }
}